package ru.clevertec.news.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.mapper.NewsMapper;
import ru.clevertec.news.service.CacheableCommentService;
import ru.clevertec.news.service.CacheableNewsService;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.service.NewsService;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultNewsServiceTest {
    private NewsService service;
    @Mock
    private CacheableNewsService cacheableNewsService;
    @Mock
    private CacheableCommentService cacheableCommentService;
    @Mock
    private CommentService commentService;
    @Mock
    private NewsMapper newsMapper;

    @BeforeEach
    void setUp() {
        service = new DefaultNewsService(cacheableNewsService, cacheableCommentService, commentService, newsMapper);
    }

    @Test
    void create() {
        CreateNews createNews = new CreateNews("user", "title", "text");
        News news = new News().setId(1);
        ResponseNews responseNews = new ResponseNews().setId(1);
        when(newsMapper.toNews(createNews)).thenReturn(news);
        when(newsMapper.toResponseNews(news)).thenReturn(responseNews);

        ResponseNews actual = service.create(createNews);

        verify(cacheableNewsService, times(1)).save(news);
        assertEquals(responseNews, actual);
    }

    @Test
    void get() {
        Pageable pageable = PageRequest.of(1, 1);
        News news = new News().setId(1);
        ResponseNewWithComments responseNewWithComments = new ResponseNewWithComments().setId(1);
        Page<ResponseComment> responseComments = new PageImpl<>(Collections.emptyList());
        when(cacheableNewsService.find(1)).thenReturn(news);
        when(newsMapper.toResponseNewWithComments(news)).thenReturn(responseNewWithComments);
        when(commentService.get(1, pageable, new SearchText())).thenReturn(responseComments);

        ResponseNewWithComments actual = service.get(1, pageable);

        assertEquals(responseNewWithComments, actual);
    }

    @Test
    void update() {
        UpdateNews updateNews = new UpdateNews("title", "text");
        News news = new News().setId(1);
        ResponseNews responseNews = new ResponseNews().setId(1);
        when(cacheableNewsService.find(1)).thenReturn(news);
        when(newsMapper.toResponseNews(news)).thenReturn(responseNews);

        ResponseNews actual = service.update(updateNews, 1);

        verify(cacheableNewsService, times(1)).evict(news);
        verify(newsMapper, times(1)).partialUpdate(updateNews, news);
        verify(cacheableNewsService, times(1)).save(news);
        assertEquals(responseNews, actual);
    }

    @Test
    void delete() {
        List<Comment> comments = List.of(new Comment().setId(1));
        News news = new News().setId(1).setComments(comments);
        when(cacheableNewsService.find(1)).thenReturn(news);

        service.delete(1);

        verify(cacheableNewsService, times(1)).delete(1);
        verify(cacheableCommentService, times(1)).evict(comments);
    }

    @Test
    void get_all() {
        Pageable pageable = PageRequest.of(1, 1);
        SearchText searchText = new SearchText("text");
        ResponseNews responseNews = new ResponseNews().setId(1);
        News news = new News().setId(1);
        Page<ResponseNews> responseNewsPage = new PageImpl<>(List.of(responseNews));
        Page<News> commentNews = new PageImpl<>(List.of(news));
        when(cacheableNewsService.findAll(pageable, searchText)).thenReturn(commentNews);
        when(newsMapper.toResponseNews(news)).thenReturn(responseNews);

        Page<ResponseNews> actual = service.get(pageable, searchText);

        assertEquals(responseNewsPage, actual);
    }
}