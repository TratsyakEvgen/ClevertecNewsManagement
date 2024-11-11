package ru.clevertec.news.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.entity.Comment;

import java.util.List;

/**
 * Сервис для управления сущностями {@link Comment} и их кэширования
 */
public interface CacheableCommentService {

    /**
     * Удаляет комментарий из кэша
     *
     * @param comment удаляемый комментарий
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует (присутствует {@link Validated}
     *                                      и комментарий не валидный
     */
    void evict(@Valid Comment comment);

    /**
     * Удаляет комментарии из кэша
     *
     * @param comments удаляемый список комментариев
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link Validated}
     *                                      и список равен null
     */
    void evict(@NotNull(message = "List comments must be not null") List<Comment> comments);

    /**
     * Удаляет комментарий с id для новости с id
     *
     * @param newsId    id новости
     * @param commentId id комментария
     */
    void delete(long newsId, long commentId);

    /**
     * Сохраняет комментарий
     *
     * @param comment сохраняемый комментарий
     * @return сохраненный комментарий
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link Validated}
     *                                      и комментарий не валидный
     */
    Comment save(@Valid Comment comment);

    /**
     * Предоставляет комментарий
     *
     * @param newsId    id новости
     * @param commentId id комментария
     * @return комментарий
     */
    @org.jetbrains.annotations.NotNull
    Comment find(long newsId, long commentId);

    /**
     * Предоставляет страницу комментариев для новости по заданным параметрам (pageable, searchText)
     *
     * @param newsId     id новости
     * @param pageable   информация об пагинации
     * @param searchText искомый текст
     * @return страница комментариев
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link Validated}
     *                                      и Pageable равен null или SearchText не валидный
     */
    @org.jetbrains.annotations.NotNull
    Page<Comment> findAll(long newsId, @NotNull(message = "Pageable must be not null") Pageable pageable,
                          @Valid SearchText searchText);
}
