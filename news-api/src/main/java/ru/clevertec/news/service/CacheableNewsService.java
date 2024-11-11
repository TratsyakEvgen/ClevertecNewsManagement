package ru.clevertec.news.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.entity.News;

/**
 * Сервис для управления сущностями {@link News} и их кэширования
 */
public interface CacheableNewsService {

    /**
     * Удаляет новость из кэша
     *
     * @param news удаляемая новость
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link Validated}
     *                                      и комментарий не валидный
     */
    void evict(@Valid News news);

    /**
     * Сохранение новости
     *
     * @param news сохраняемая новость
     * @return сохраненная новость
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link Validated}
     *                                      и новость не валидная
     */
    News save(@Valid News news);

    /**
     * Предоставляет страницу новостей заданным параметрам (pageable, searchText)
     *
     * @param pageable   информация об пагинации
     * @param searchText искомый текст
     * @return страница новостей
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link Validated}
     *                                      и Pageable равен null или SearchText не валидный
     */
    @org.jetbrains.annotations.NotNull
    Page<News> findAll(@NotNull(message = "Pageable must be not null") Pageable pageable,
                       @Valid SearchText searchText);

    /**
     * Предоставляет новость по id
     *
     * @param id id новости
     * @return новость
     */
    @org.jetbrains.annotations.NotNull
    News find(long id);

    /**
     * Удаляет новость по id
     *
     * @param id id новости
     */
    void delete(long id);
}
