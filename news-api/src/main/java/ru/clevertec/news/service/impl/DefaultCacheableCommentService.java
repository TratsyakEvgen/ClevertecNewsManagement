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
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.service.CacheableCommentService;

@Service
@RequiredArgsConstructor
@Validated
public class DefaultCacheableCommentService implements CacheableCommentService {
    private final CommentRepository commentRepository;
    private final CacheManager cacheManager;

    @Override
    @CacheEvict(value = "comments", key = "#comment.id")
    public void evict(Comment comment) {
    }

    @Override
    @CacheEvict(value = "comments", key = "#commentId")
    public void delete(long newsId, long commentId) {
        commentRepository.deleteByNewsIdAndId(newsId, commentId);
    }

    @Override
    @CachePut(value = "comments", key = "#comment.id")
    public void save(Comment comment) {
        commentRepository.save(comment);
    }

    @Override
    @Cacheable(value = "comments", key = "#commentId")
    public Comment get(long newsId, long commentId) {
        return commentRepository.findByNewsIdAndId(newsId, commentId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Comment with id %d in news with id %d not found", commentId, newsId)
                ));
    }

    @Override
    public Page<Comment> findAll(long newsId, Pageable pageable, SearchText searchText) {
        Page<Comment> commentsPage = commentRepository.findByNewsIdWithText(newsId, pageable, searchText);
        putContent(commentsPage);
        return commentsPage;
    }

    private void putContent(Page<Comment> commentsPage) {
        Cache cache = cacheManager.getCache("comments");
        if (cache != null) {
            commentsPage.forEach(comment -> cache.put(comment.getId(), comment));
        }
    }

}
