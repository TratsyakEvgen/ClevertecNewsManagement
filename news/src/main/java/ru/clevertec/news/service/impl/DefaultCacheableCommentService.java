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
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.service.CacheableCommentService;
import ru.clevertec.news.service.ServiceException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
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
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @Cacheable(value = "comments", key = "#commentId")
    public Comment get(long newsId, long commentId) {
        return commentRepository.findByNewsIdAndId(newsId, commentId)
                .orElseThrow(() -> new ServiceException(
                        String.format("Comment with id %d in news with id %d not found", commentId, newsId)
                ));
    }

    @Override
    public Page<Comment> get(long newsId, Pageable pageable, Filter filter) {
        Filter currentFilter = Optional.ofNullable(filter)
                .orElseGet(Filter::new);
        Page<Comment> commentsPage = commentRepository.findByNewsIdUseFilter(newsId, pageable, currentFilter);
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
