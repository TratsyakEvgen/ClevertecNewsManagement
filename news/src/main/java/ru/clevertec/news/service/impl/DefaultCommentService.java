package ru.clevertec.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.dto.response.ResponsePage;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.mapper.CommentMapper;
import ru.clevertec.news.service.CommentEntityService;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.service.ServiceException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultCommentService implements CommentService {
    private final CommentEntityService commentEntityService;
    private final CommentMapper commentMapper;

    @Override
    public void delete(long newsId, long commentId) {
        commentEntityService.delete(newsId, commentId);
    }

    @Override
    public ResponseComment update(long newsId, long commentId, UpdateComment updateComment) {
        Comment comment = commentEntityService.get(newsId, commentId);

        return Optional.ofNullable(updateComment)
                .map(update -> commentMapper.partialUpdate(update, comment))
                .map(commentEntityService::save)
                .map(commentMapper::toResponseComment)
                .orElseThrow(() -> new ServiceException("UpdateCommit must not be null"));
    }

    @Override
    public ResponseComment create(long newsId, CreateComment createComment) {
        return Optional.ofNullable(createComment)
                .map(commentMapper::toComment)
                .map(comment -> comment.setLocalDateTime(LocalDateTime.now()))
                .map(commentEntityService::save)
                .map(commentMapper::toResponseComment)
                .orElseThrow(() -> new ServiceException("CreateComment  must not be null"));
    }

    @Override
    public ResponseComment get(long newsId, long commentId) {
        Comment comment = commentEntityService.get(newsId, commentId);
        return commentMapper.toResponseComment(comment);
    }

    @Override
    public ResponsePage<ResponseComment> get(long newsId, Pageable pageable, Filter filter) {
        Page<Comment> comments = commentEntityService.get(newsId, pageable, filter);
        return commentMapper.toResponsePage(comments);
    }
}
