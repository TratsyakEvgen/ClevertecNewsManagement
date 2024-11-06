package ru.clevertec.news.service.impl;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
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

import java.util.Optional;

/**
 * Сервис для управления сущностями {@link News} и их кэширования
 */
@Service
@RequiredArgsConstructor
@Validated
public class DefaultCacheableNewsService implements CacheableNewsService {
    private final NewsRepository newsRepository;
    private final CacheManager cacheManager;

    /**
     * Удаляет новость из кэша по id
     *
     * @param news удаляемая новость
     * @throws ConstraintViolationException если новость не валидная
     */
    @Override
    @CacheEvict(value = "news", key = "#news.id")
    public void evict(News news) {
    }

    /**
     * Сохраняет новость в репозитории, затем в кэше по id
     *
     * @param news сохраняемая новость
     * @return сохраненная новость
     * @throws ConstraintViolationException если новость не валидная
     */
    @Override
    @CachePut(value = "news", key = "#news.id")
    public News save(News news) {
        return newsRepository.save(news);
    }

    /**
     * Предоставляет страницу новостей заданным параметрам (pageable, searchText), затем все найденные новости в кэшируются
     *
     * @param pageable   информация об пагинации
     * @param searchText искомый текст
     * @return страница новостей
     * @throws ConstraintViolationException если Pageable равен null или SearchText не валидный
     */
    @Override
    public @NotNull Page<News> findAll(Pageable pageable, SearchText searchText) {
        Page<News> newsPage = newsRepository.findAllWithText(pageable, searchText);
        putContentInCache(newsPage);
        return newsPage;
    }

    /**
     * Предоставляет новость из кэша по id. Если новость не найден в кэше, то она берется из репозитория
     *
     * @param id id новости
     * @return новость
     * @throws EntityNotFoundException если новость с id не найдена
     */
    @Override
    @Cacheable(value = "news", key = "#id")
    public @NotNull News find(long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("News with id " + id + " not found"));
    }

    /**
     * Удаляет новость из репозитория, а затем и из кэша по ее id
     *
     * @param id id новости
     */
    @Override
    @CacheEvict(value = "news", key = "#id")
    public void delete(long id) {
        newsRepository.deleteById(id);
    }

    /**
     * Сохранение в кэше все новости из страницы по их id
     *
     * @param newsPage страница новостей
     */
    private void putContentInCache(Page<News> newsPage) {
        Optional.ofNullable(cacheManager.getCache("news"))
                .ifPresent(cache -> newsPage.forEach(news -> cache.put(news.getId(), news)));
    }
}
