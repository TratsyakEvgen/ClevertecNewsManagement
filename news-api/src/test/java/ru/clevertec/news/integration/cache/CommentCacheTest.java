package ru.clevertec.news.integration.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.service.CommentService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public abstract class CommentCacheTest {
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CommentService commentService;
    @SpyBean
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        Objects.requireNonNull(cacheManager.getCache("comments")).clear();
    }

    @Test
    void delete_shouldDeleteCommentFromCache() {
        commentService.get(1, 1);

        commentService.delete(1, 1);

        verify(commentRepository, times(1)).findByNewsIdAndId(1, 1);
        assertThrows(EntityNotFoundException.class, () -> commentService.get(1, 1));
    }

    @Test
    void delete_ifRepositoryThrow_shouldNotDeleteCommentFromCache() {
        commentService.get(1, 1);
        doThrow(RuntimeException.class).when(commentRepository).deleteByNewsIdAndId(1, 1);

        try {
            commentService.delete(1, 1);
        } catch (RuntimeException ignored) {
        }
        commentService.get(1, 1);

        verify(commentRepository, times(1)).findByNewsIdAndId(1, 1);
    }

    @Test
    void update_ifRepositoryThrow_shouldNotAddCommentInCache() {
        UpdateComment updateComment = new UpdateComment("text");
        doThrow(RuntimeException.class).when(commentRepository).save(any());

        try {
            commentService.update(1, 1, updateComment);
        } catch (RuntimeException ignored) {
        }
        commentService.get(1, 1);

        verify(commentRepository, times(2)).findByNewsIdAndId(1, 1);
    }

    @Test
    void update_shouldAddCommentInCache() {
        UpdateComment updateComment = new UpdateComment("text");

        commentService.update(1, 1, updateComment);
        commentService.get(1, 1);

        verify(commentRepository, times(1)).save(any());
        verify(commentRepository, times(1)).findByNewsIdAndId(1, 1);

    }

    @Test
    void create_shouldAddCommentInCache() {
        CreateComment createComment = new CreateComment("user", "text");

        ResponseComment comment = commentService.create(1, createComment);
        commentService.get(comment.getNewsId(), comment.getId());

        verify(commentRepository, times(1)).save(any());
        verify(commentRepository, times(0)).findByNewsIdAndId(comment.getNewsId(), comment.getId());

    }

    @Test
    void get_shouldAddCommentInCache() {
        commentService.get(1, 1);

        commentService.get(1, 1);

        verify(commentRepository, times(1)).findByNewsIdAndId(1, 1);
    }

    @Test
    void getAll_shouldSaveAllInCache() {
        List<ResponseComment> content = commentService.get(1, PageRequest.of(0, 10), new SearchText()).getContent();

        content.forEach(comment -> commentService.get(comment.getNewsId(), comment.getId()));

        verify(commentRepository, times(0)).findByNewsIdAndId(1, 1);
    }
}
