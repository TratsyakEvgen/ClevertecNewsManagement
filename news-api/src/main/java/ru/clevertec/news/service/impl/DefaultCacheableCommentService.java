package ru.clevertec.news.service.impl;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.service.CacheableCommentService;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления сущностями {@link Comment} и их кэширования
 */
@Service
@RequiredArgsConstructor
@Validated
@Slf4j
public class DefaultCacheableCommentService implements CacheableCommentService {
    private final CommentRepository commentRepository;
    private final CacheManager cacheManager;

    /**
     * Удаляет комментарий из кэша согласно его id
     *
     * @param comment удаляемый комментарий
     * @throws ConstraintViolationException если комментарий не валидный
     */
    @Override
    @CacheEvict(value = "comments", key = "{#comment.id, #comment.news.id}")
    public void evict(Comment comment) {
        log.debug("CacheableCommentService: evict comment " + comment);
    }

    /**
     * Удаляет комментарии из кэша согласно их id
     *
     * @param comments удаляемый список комментариев
     * @throws ConstraintViolationException если список равен null
     */
    @Override
    public void evict(List<Comment> comments) {
        log.debug("CacheableCommentService: evict comments " + comments);
        Optional.ofNullable(cacheManager.getCache("comments"))
                .ifPresent(cache -> comments.forEach(comment ->
                        cache.evict(List.of(comment.getId(), comment.getNews().getId()))
                ));
    }

    /**
     * Удаляет комментарий из репозитория, а затем и из кэша по его id
     *
     * @param newsId    id новости
     * @param commentId id комментария
     */
    @Override
    @CacheEvict(value = "comments", key = "{#commentId, #newsId}")
    public void delete(long newsId, long commentId) {
        log.debug(String.format("CacheableCommentService: delete comment %d for news %d", commentId, newsId));
        commentRepository.deleteByNewsIdAndId(newsId, commentId);
    }

    /**
     * Сохраняет комментарий в репозитории, а затем и в кэше по его id
     *
     * @param comment сохраняемый комментарий
     * @return сохраненный комментарий
     * @throws ConstraintViolationException если включена валидация в имплементации (@Validation) и комментарий не валидный
     */
    @Override
    @CachePut(value = "comments", key = "{#comment.id, #comment.news.id}")
    public Comment save(Comment comment) {
        log.debug("CacheableCommentService: save comment " + comment);
        Comment saved = commentRepository.save(comment);
        log.debug("CacheableCommentService: save comment result " + saved);
        return saved;
    }

    /**
     * Предоставляет комментарий из кэша по id. Если комментарий не найден в кэше, то он берется из репозитория
     *
     * @param newsId    id новости
     * @param commentId id комментария
     * @return комментарий
     * @throws EntityNotFoundException если комментарий с commentId не найден в новости с newsId
     */
    @Override
    @Cacheable(value = "comments", key = "{#commentId, #newsId}")
    public @NotNull Comment find(long newsId, long commentId) {
        log.debug(String.format("CacheableCommentService: find comment %d for news %d", commentId, newsId));
        Comment comment = commentRepository.findByNewsIdAndId(newsId, commentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Comment with id %d in news with id %d not found", commentId, newsId)
                ));
        log.debug(String.format(
                "CacheableCommentService: find comment %d for news %d, result %s", commentId, newsId, comment
        ));
        return comment;
    }

    /**
     * Предоставляет страницу комментариев для новости по заданным параметрам(pageable, searchText),
     * затем все найденные комментарии кэшируются
     *
     * @param newsId     id новости
     * @param pageable   информация об пагинации
     * @param searchText искомый текст
     * @return страница комментариев
     * @throws ConstraintViolationException если Pageable равен null или SearchText не валидный
     */
    @Override
    public @NotNull Page<Comment> findAll(long newsId, Pageable pageable, SearchText searchText) {
        log.debug(String.format(
                "CacheableCommentService: findAll newsId %d, pageable %s, searchText %s ",
                newsId, pageable, searchText)
        );
        Page<Comment> commentsPage = commentRepository.findByNewsIdWithText(newsId, pageable, searchText);
        putContentToCache(commentsPage);
        log.debug(String.format(
                "CacheableCommentService: findAll newsId %d, pageable %s, searchText %s, result %s",
                newsId, pageable, searchText, commentsPage)
        );
        return commentsPage;
    }

    /**
     * Сохраняет в кэше все комментарии из страницы по их id
     *
     * @param commentsPage страница комментариев
     */
    private void putContentToCache(Page<Comment> commentsPage) {
        log.debug("CacheableCommentService: put content comment page to cache " + commentsPage);
        Optional.ofNullable(cacheManager.getCache("comments"))
                .ifPresent(cache -> commentsPage.forEach(comment ->
                        cache.put(List.of(comment.getId(), comment.getNews().getId()), comment))
                );
    }

}
