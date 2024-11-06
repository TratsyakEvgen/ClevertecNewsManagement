package ru.clevertec.news.unit.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.mapper.CommentMapper;
import ru.clevertec.news.service.CacheableCommentService;
import ru.clevertec.news.service.CacheableNewsService;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.service.impl.DefaultCommentService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCommentServiceTest {
    private CommentService service;
    @Mock
    private CacheableCommentService cacheableCommentService;
    @Mock
    private CacheableNewsService cacheableNewsService;
    @Mock
    private CommentMapper commentMapper;

    @BeforeEach
    void setUp() {
        service = new DefaultCommentService(cacheableCommentService, cacheableNewsService, commentMapper);
    }

    @Test
    void delete() {
        service.delete(1, 1);

        verify(cacheableCommentService, times(1)).delete(1, 1);
    }

    @Test
    void update() {
        UpdateComment updateComment = new UpdateComment("text");
        Comment comment = new Comment().setId(1);
        ResponseComment responseComment = new ResponseComment().setId(1);
        when(cacheableCommentService.find(1, 1)).thenReturn(comment);
        when(commentMapper.toResponseComment(comment)).thenReturn(responseComment);

        ResponseComment actual = service.update(1, 1, updateComment);

        verify(cacheableCommentService, times(1)).evict(comment);
        verify(commentMapper, times(1)).partialUpdate(updateComment, comment);
        verify(cacheableCommentService, times(1)).save(comment);
        assertEquals(responseComment, actual);
    }

    @Test
    void create() {
        CreateComment createComment = new CreateComment("user", "text");
        Comment comment = new Comment().setId(1);
        ResponseComment responseComment = new ResponseComment().setId(1);
        when(commentMapper.toComment(createComment)).thenReturn(comment);
        when(commentMapper.toResponseComment(comment)).thenReturn(responseComment);

        ResponseComment actual = service.create(1, createComment);

        verify(cacheableNewsService, times(1)).find(1);
        verify(cacheableCommentService, times(1)).save(comment);
        assertEquals(responseComment, actual);
    }

    @Test
    void create_ifNewsNotFound() {
        CreateComment createComment = new CreateComment("user", "text");
        Comment comment = new Comment().setId(1);
        when(commentMapper.toComment(createComment)).thenReturn(comment);
        when(cacheableNewsService.find(1)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,() ->service.create(1, createComment) );
    }

    @Test
    void get() {
        Comment comment = new Comment().setId(1);
        ResponseComment responseComment = new ResponseComment().setId(1);
        when(cacheableCommentService.find(1, 1)).thenReturn(comment);
        when(commentMapper.toResponseComment(comment)).thenReturn(responseComment);

        ResponseComment actual = service.get(1, 1);

        assertEquals(responseComment, actual);
    }

    @Test
    void get_all() {
        Pageable pageable = PageRequest.of(1, 1);
        SearchText searchText = new SearchText("text");
        ResponseComment responseComment = new ResponseComment().setId(1);
        Comment comment = new Comment().setId(1);
        Page<ResponseComment> responseComments = new PageImpl<>(List.of(responseComment));
        Page<Comment> commentPage = new PageImpl<>(List.of(comment));
        when(cacheableCommentService.findAll(1, pageable, searchText)).thenReturn(commentPage);
        when(commentMapper.toResponseComment(comment)).thenReturn(responseComment);

        Page<ResponseComment> actual = service.get(1, pageable, searchText);

        assertEquals(responseComments, actual);
    }
}