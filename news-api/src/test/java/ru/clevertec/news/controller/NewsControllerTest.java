package ru.clevertec.news.controller;

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
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;
import ru.clevertec.news.service.NewsService;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsControllerTest {
    private NewsController newsController;
    @Mock
    private NewsService newsService;

    @BeforeEach
    void setUp() {
        newsController = new NewsController(newsService);
    }

    @Test
    void getAllNews() {
        Pageable pageable = PageRequest.of(1, 1);
        SearchText searchText = new SearchText("text");
        Page<ResponseNews> page = new PageImpl<>(Collections.emptyList());
        when(newsService.get(pageable, searchText)).thenReturn(page);

        Page<ResponseNews> actual = newsController.getAllNews(pageable, searchText);

        assertEquals(page, actual);
    }

    @Test
    void getNews() {
        Pageable pageable = PageRequest.of(1, 1);
        ResponseNewWithComments responseNews = new ResponseNewWithComments().setId(1);
        when(newsService.get(1, pageable)).thenReturn(responseNews);

        ResponseNewWithComments actual = newsController.getNews(1, pageable);

        assertEquals(responseNews, actual);
    }

    @Test
    void createNews() {
        CreateNews createNews = new CreateNews("user", "title", "text");
        ResponseNews responseNews = new ResponseNews().setId(1);
        when(newsService.create(createNews)).thenReturn(responseNews);

        ResponseNews actual = newsController.createNews(createNews);

        assertEquals(responseNews, actual);
    }

    @Test
    void updateNews() {
        UpdateNews updateNews = new UpdateNews("title", "text");
        ResponseNews responseNews = new ResponseNews().setId(1);
        when(newsService.update(updateNews, 1)).thenReturn(responseNews);

        ResponseNews actual = newsController.updateNews(updateNews, 1);

        assertEquals(responseNews, actual);
    }

    @Test
    void deleteNews() {
        newsController.deleteNews(1);

        verify(newsService, times(1)).delete(1);
    }
}