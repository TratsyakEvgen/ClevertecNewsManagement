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
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.dto.response.ResponseNews;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.service.NewsService;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
public abstract class NewsCacheTest {
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private NewsService newsService;
    @Autowired
    private CommentService commentService;
    @SpyBean
    private NewsRepository newsRepository;
    @SpyBean
    private CommentRepository commentRepository;

    @BeforeEach
    void setUp() {
        Objects.requireNonNull(cacheManager.getCache("news")).clear();
        Objects.requireNonNull(cacheManager.getCache("comments")).clear();
    }

    @Test
    void delete_shouldDeleteNewsFromCache() {
        newsService.get(1, PageRequest.of(0, 10));

        newsService.delete(1);

        verify(newsRepository, times(1)).findById(1L);
        assertThrows(EntityNotFoundException.class, () -> newsService.get(1, PageRequest.of(0, 10)));
    }

    @Test
    void delete_shouldDeleteCommentsFromCache() {
        List<ResponseComment> content = newsService.get(1, PageRequest.of(0, 10)).getComments().getContent();

        newsService.delete(1);

        content.forEach(comment ->
                assertThrows(EntityNotFoundException.class, () -> commentService.get(1, comment.getId()))
        );

    }

    @Test
    void delete_ifRepositoryThrow_shouldNotDeleteNewsFromCache() {
        newsService.get(1, PageRequest.of(0, 10));
        doThrow(RuntimeException.class).when(newsRepository).deleteById(1L);

        try {
            newsService.delete(1);
        } catch (RuntimeException ignored) {
        }
        newsService.get(1, PageRequest.of(0, 10));

        verify(newsRepository, times(1)).findById(1L);
    }

    @Test
    void delete_ifRepositoryThrow_shouldNotDeleteCommentsFromCache() {
        List<ResponseComment> content = newsService.get(1, PageRequest.of(0, 10)).getComments().getContent();
        doThrow(RuntimeException.class).when(newsRepository).deleteById(1L);

        try {
            newsService.delete(1);
        } catch (RuntimeException ignored) {
        }
        content.forEach(comment -> commentService.get(1, comment.getId()));

        verify(commentRepository, times(0)).findByNewsIdAndId(anyLong(), anyLong());
    }

    @Test
    void update_ifRepositoryThrow_shouldNotAddNewsInCache() {
        UpdateNews updateNews = new UpdateNews("title", "text");
        doThrow(RuntimeException.class).when(newsRepository).save(any());

        try {
            newsService.update(updateNews, 1);
        } catch (RuntimeException ignored) {
        }
        newsService.get(1, PageRequest.of(0, 10));

        verify(newsRepository, times(2)).findById(1L);
    }

    @Test
    void update_shouldAddNewsInCache() {
        UpdateNews updateNews = new UpdateNews("title", "text");

        newsService.update(updateNews, 1);
        newsService.get(1, PageRequest.of(0, 10));

        verify(newsRepository, times(1)).save(any());
        verify(newsRepository, times(1)).findById(1L);

    }

    @Test
    void create_shouldAddNewsInCache() {
        CreateNews createNews = new CreateNews("user", "title", "text");

        ResponseNews news = newsService.create(createNews);
        newsService.get(news.getId(), PageRequest.of(0, 10));

        verify(newsRepository, times(1)).save(any());
        verify(newsRepository, times(0)).findById(news.getId());

    }

    @Test
    void get_shouldAddNewsInCache() {
        newsService.get(1, PageRequest.of(0, 10));

        newsService.get(1, PageRequest.of(0, 10));

        verify(newsRepository, times(1)).findById(1L);
    }

    @Test
    void get_shouldAddCommentsInCache() {
        List<ResponseComment> content = newsService.get(1, PageRequest.of(0, 10)).getComments().getContent();

        newsService.get(1, PageRequest.of(0, 10));

        content.forEach(comment -> commentService.get(1, comment.getId()));
        verify(commentRepository, times(0)).findByNewsIdAndId(anyLong(), anyLong());
    }

    @Test
    void getAll_shouldSaveAllNewsInCache() {
        List<ResponseNews> newsList = newsService.get(PageRequest.of(0, 10), new SearchText()).getContent();

        newsList.forEach(news -> newsService.get(news.getId(), PageRequest.of(0, 10)));

        verify(newsRepository, times(0)).findById(1L);
    }
}
