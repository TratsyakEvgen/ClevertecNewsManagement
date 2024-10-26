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
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.repository.NewsRepository;
import ru.clevertec.news.service.CacheableNewsService;
import ru.clevertec.news.service.ServiceException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultCacheableNewsService implements CacheableNewsService {
    private final NewsRepository newsRepository;
    private final CacheManager cacheManager;

    @Override
    @CacheEvict(value = "news", key = "#news.id")
    public void evict(News news) {
    }

    @Override
    @CachePut(value = "news", key = "#news.id")
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Override
    public Page<News> findAll(Pageable pageable, Filter filter) {
        Filter currentFilter = Optional.ofNullable(filter)
                .orElseGet(Filter::new);
        Page<News> newsPage = newsRepository.findAllUseFilter(pageable, currentFilter);
        putContent(newsPage);
        return newsPage;
    }

    @Override
    @Cacheable(value = "news", key = "#id")
    public News findById(long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new ServiceException("News with id " + id + " not found"));
    }

    @Override
    @CacheEvict(value = "news", key = "#id")
    public void deleteById(long id) {
        newsRepository.deleteById(id);
    }

    private void putContent(Page<News> newsPage) {
        Cache cache = cacheManager.getCache("news");
        if (cache != null) {
            newsPage.forEach(news -> cache.put(news.getId(), news));
        }
    }
}
