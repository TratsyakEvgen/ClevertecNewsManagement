package ru.clevertec.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.CacheableNewsService;

@Service
@RequiredArgsConstructor
@Validated
public class DefaultCacheableNewsService implements CacheableNewsService {
    private final NewsRepository newsRepository;
    private final CacheManager cacheManager;

    @Override
    @CacheEvict(value = "news", key = "#news.id")
    public void evict(News news) {
    }

    @Override
    @CachePut(value = "news", key = "#news.id")
    public void save(News news) {
        newsRepository.save(news);
    }

    @Override
    public Page<News> findAll(Pageable pageable, SearchText searchText) {
        Page<News> newsPage = newsRepository.findAllWithText(pageable, searchText);
        putContentInCache(newsPage);
        return newsPage;
    }

    @Override
    @Cacheable(value = "news", key = "#id")
    public News find(long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + id + " not found"));
    }

    @Override
    @CacheEvict(value = "news", key = "#id")
    public void delete(long id) {
        newsRepository.deleteById(id);
    }

    private void putContentInCache(Page<News> newsPage) {
        Cache cache = cacheManager.getCache("news");
        if (cache != null) {
            newsPage.forEach(news -> cache.put(news.getId(), news));
        }
    }
}
