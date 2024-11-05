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
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.service.CacheableCommentService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultCacheableCommentServiceTest {
    private CacheableCommentService service;
    @Mock
    private CommentRepository repository;
    @Mock
    private CacheManager cacheManager;
    @Mock
    private Cache cache;

    @BeforeEach
    void setUp() {
        service = new DefaultCacheableCommentService(repository, cacheManager);
    }

    @Test
    void evict() {
        List<Comment> comments = List.of(new Comment().setId(1), new Comment().setId(2));
        when(cacheManager.getCache(any())).thenReturn(cache);

        service.evict(comments);

        verify(cache, times(1)).evict(1L);
        verify(cache, times(1)).evict(2L);
    }

    @Test
    void delete() {
        service.delete(1, 1);

        verify(repository, times(1)).deleteByNewsIdAndId(1, 1);
    }

    @Test
    void save() {
        Comment comment = new Comment().setId(1);

        service.save(comment);

        verify(repository, times(1)).save(comment);
    }

    @Test
    void find_ifEntityIsPresent() {
        Comment comment = new Comment().setId(1);
        when(repository.findByNewsIdAndId(1, 1)).thenReturn(Optional.of(comment));

        Comment actual = service.find(1, 1);

        verify(repository, times(1)).findByNewsIdAndId(1, 1);
        assertEquals(comment, actual);
    }

    @Test
    void find_ifEntityIsNotPresent() {
        when(repository.findByNewsIdAndId(1, 1)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.find(1, 1));
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(1, 2);
        SearchText searchText = new SearchText("text");
        Page<Comment> commentPage = new PageImpl<>(List.of(new Comment().setId(1), new Comment().setId(2)));
        when(repository.findByNewsIdWithText(1, pageable, searchText)).thenReturn(commentPage);
        when(cacheManager.getCache(any())).thenReturn(cache);

        Page<Comment> actual = service.findAll(1L, pageable, searchText);

        verify(cache, times(1)).put(eq(1L), any());
        verify(cache, times(1)).put(eq(2L), any());
        assertEquals(commentPage, actual);
    }

    @Test
    void findAll_ifNotFound() {
        Pageable pageable = PageRequest.of(1, 1);
        SearchText searchText = new SearchText("text");
        Page<Comment> commentPage = new PageImpl<>(Collections.emptyList());
        when(repository.findByNewsIdWithText(1, pageable, searchText)).thenReturn(commentPage);
        when(cacheManager.getCache(any())).thenReturn(cache);

        Page<Comment> actual = service.findAll(1, pageable, searchText);

        verify(cache, times(0)).put(any(), any());
        assertEquals(commentPage, actual);
    }
}