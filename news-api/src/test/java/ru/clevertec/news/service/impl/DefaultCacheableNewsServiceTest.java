package ru.clevertec.news.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.CacheableNewsService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCacheableNewsServiceTest {
    private CacheableNewsService service;
    @Mock
    private NewsRepository repository;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private Cache cache;

    @BeforeEach
    void setUp() {
        service = new DefaultCacheableNewsService(repository, cacheManager);
    }

    @Test
    void save() {
        News news = new News().setId(1);

        service.save(news);

        verify(repository, times(1)).save(news);
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(1, 2);
        SearchText searchText = new SearchText("text");
        Page<News> newsPage = new PageImpl<>(List.of(new News().setId(1), new News().setId(2)));
        when(repository.findAllWithText(pageable, searchText)).thenReturn(newsPage);
        when(cacheManager.getCache(any())).thenReturn(cache);

        Page<News> actual = service.findAll(pageable, searchText);

        verify(cache, times(1)).put(eq(1L), any());
        verify(cache, times(1)).put(eq(2L), any());
        assertEquals(newsPage, actual);
    }

    @Test
    void findAll_ifNotFound() {
        Pageable pageable = PageRequest.of(1, 1);
        SearchText searchText = new SearchText("text");
        Page<News> newsPage = new PageImpl<>(Collections.emptyList());
        when(repository.findAllWithText(pageable, searchText)).thenReturn(newsPage);
        when(cacheManager.getCache(any())).thenReturn(cache);

        Page<News> actual = service.findAll(pageable, searchText);

        verify(cache, times(0)).put(any(), any());
        assertEquals(newsPage, actual);
    }

    @Test
    void find_ifEntityIsPresent() {
        News news = new News().setId(1);
        when(repository.findById(1L)).thenReturn(Optional.of(news));

        News actual = service.find(1);

        verify(repository, times(1)).findById(1L);
        assertEquals(news, actual);
    }

    @Test
    void find_ifEntityIsNotPresent() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.find(1L));
    }

    @Test
    void delete() {
        service.delete(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}