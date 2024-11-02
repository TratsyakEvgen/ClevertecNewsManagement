package ru.clevertec.news.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;

/**
 * Сервис управления новостями
 */
public interface NewsService {
    /**
     * Создание новости
     *
     * @param createNews информация о создании новости
     * @return новость
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link org.springframework.validation.annotation.Validated}) и createNews не валидный
     */
    ResponseNews create(@Valid CreateNews createNews);

    /**
     * Предоставляет страницу новостей по заданным параметрам (pageable, searchText)
     *
     * @param pageable   информация об пагинации
     * @param searchText искомый текст
     * @return страница новостей
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link org.springframework.validation.annotation.Validated}) и Pageable равен null или SearchText не валидный
     */
    Page<ResponseNews> get(@NotNull(message = "Pageable must be not null") Pageable pageable,
                           @Valid SearchText searchText);

    /**
     * Обновление новости
     *
     * @param updateNews информация об обновлении новости
     * @param id         id новости
     * @return новость
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link org.springframework.validation.annotation.Validated}) и updateNews не валидный
     */
    ResponseNews update(@Valid UpdateNews updateNews, long id);

    /**
     * Удаление новости
     *
     * @param id id новости
     */
    void delete(long id);

    /**
     * Предоставляет новость со страницей комментариев
     *
     * @param id       id новости
     * @param pageable информация об пагинации комментариев
     * @return новость со страницей комментариев
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link org.springframework.validation.annotation.Validated}) и Pageable равен null
     */
    ResponseNewWithComments get(long id, @NotNull(message = "Pageable must be not null") Pageable pageable);
}
