package ru.clevertec.news.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;

/**
 * Сервис для управления комментариями
 */
public interface CommentService {
    /**
     * Удаляет заданный комментарий для новости
     *
     * @param newsId    id новости
     * @param commentId id комментария
     */
    void delete(long newsId, long commentId);

    /**
     * Обновляет заданный комментарий для новости
     *
     * @param newsId        id новости
     * @param commentId     id комментария
     * @param updateComment обновляемая информация
     * @return комментарий
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link Validated}
     *                                      и updateComment не валиден
     */
    ResponseComment update(long newsId, long commentId, @Valid UpdateComment updateComment);

    /**
     * Создает комментарий для заданной новости
     *
     * @param newsId        id новости
     * @param createComment информация о создании комментария
     * @return комментарий
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link Validated}
     *                                      и createComment не валиден
     */
    ResponseComment create(long newsId, @Valid CreateComment createComment);

    /**
     * Предоставляет заданный комментарий для новости
     *
     * @param newsId    id новости
     * @param commentId id комментария
     * @return комментарий
     */
    ResponseComment get(long newsId, long commentId);

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
    Page<ResponseComment> get(long newsId, @NotNull(message = "Pageable must be not null") Pageable pageable,
                              @Valid SearchText searchText);

}
