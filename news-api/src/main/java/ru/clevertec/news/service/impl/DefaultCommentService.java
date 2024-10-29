package ru.clevertec.news.service.impl;

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

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class DefaultCommentService implements CommentService {
    private final CacheableCommentService cacheableCommentService;
    private final CommentMapper commentMapper;

    @Override
    public void delete(long newsId, long commentId) {
        cacheableCommentService.delete(newsId, commentId);
    }

    @Override
    public ResponseComment update(long newsId, long commentId, UpdateComment updateComment) {
        Comment comment = cacheableCommentService.get(newsId, commentId);
        cacheableCommentService.evict(comment);
        commentMapper.partialUpdate(updateComment, comment);
        cacheableCommentService.save(comment);
        return commentMapper.toResponseComment(comment);
    }

    @Override
    public ResponseComment create(long newsId, CreateComment createComment) {
        Comment comment = commentMapper.toComment(createComment);
        comment.setDate(LocalDateTime.now());
        cacheableCommentService.save(comment);
        return commentMapper.toResponseComment(comment);
    }

    @Override
    public ResponseComment get(long newsId, long commentId) {
        Comment comment = cacheableCommentService.get(newsId, commentId);
        return commentMapper.toResponseComment(comment);
    }

    @Override
    public Page<ResponseComment> get(long newsId, Pageable pageable, SearchText searchText) {
        Page<Comment> comments = cacheableCommentService.findAll(newsId, pageable, searchText);
        return commentMapper.toResponsePage(comments);
    }
}
