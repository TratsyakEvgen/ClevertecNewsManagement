package ru.clevertec.news.service.impl;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.mapper.CommentMapper;
import ru.clevertec.news.service.CacheableCommentService;
import ru.clevertec.news.service.CommentService;

import java.time.LocalDateTime;

/**
 * Сервис для управления комментариями
 */
@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class DefaultCommentService implements CommentService {
    private final CacheableCommentService cacheableCommentService;
    private final CommentMapper commentMapper;

    /**
     * Удаляет заданный комментарий для новости
     *
     * @param newsId    id новости
     * @param commentId id комментария
     */
    @Override
    public void delete(long newsId, long commentId) {
        cacheableCommentService.delete(newsId, commentId);
    }

    /**
     * Обновляет заданный комментарий для новости.
     * Обновление подлежат поля переданные в updateComment (null поля не обновляются).
     * Перед изменениями комментарий удаляется из кэша,
     * так как в случае исключения при сохранении в кэше останутся не консистентные данные.
     * После сохранения данные заново кэшируются
     *
     * @param newsId        id новости
     * @param commentId     id комментария
     * @param updateComment обновляемая информация
     * @return обновленный комментарий
     * @throws ConstraintViolationException если updateComment не валиден
     */
    @Override
    public ResponseComment update(long newsId, long commentId, UpdateComment updateComment) {
        Comment comment = cacheableCommentService.find(newsId, commentId);
        cacheableCommentService.evict(comment);
        commentMapper.partialUpdate(updateComment, comment);
        cacheableCommentService.save(comment);
        return commentMapper.toResponseComment(comment);
    }

    /**
     * Создание комментария.
     *
     * @param newsId        id новости
     * @param createComment информация о создании комментария
     * @return созданный комментарий
     * @throws ConstraintViolationException если createComment не валиден
     */
    @Override
    public ResponseComment create(long newsId, CreateComment createComment) {
        Comment comment = commentMapper.toComment(createComment);
        comment.setDate(LocalDateTime.now());
        cacheableCommentService.save(comment);
        return commentMapper.toResponseComment(comment);
    }

    /**
     * Предоставляет заданный комментарий для новости
     *
     * @param newsId    id новости
     * @param commentId id комментария
     * @return комментарий
     */
    @Override
    public ResponseComment get(long newsId, long commentId) {
        Comment comment = cacheableCommentService.find(newsId, commentId);
        return commentMapper.toResponseComment(comment);
    }

    /**
     * Предоставляет страницу комментариев для новости по заданным параметрам (pageable, searchText)
     *
     * @param newsId     id новости
     * @param pageable   информация об пагинации
     * @param searchText искомый текст
     * @return страница комментариев
     * @throws ConstraintViolationException если Pageable равен null или SearchText не валидный
     */
    @Override
    public Page<ResponseComment> get(long newsId, Pageable pageable, SearchText searchText) {
        Page<Comment> commentPage = cacheableCommentService.findAll(newsId, pageable, searchText);
        return commentPage.map(commentMapper::toResponseComment);
    }
}
