package ru.clevertec.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.repository.CommentRepository;
import ru.clevertec.news.service.CommentEntityService;
import ru.clevertec.news.service.ServiceException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultCommentEntityService implements CommentEntityService {
    private final CommentRepository commentRepository;

    @Override
    public void delete(long newsId, long commentId) {
        commentRepository.deleteByNewsIdAndId(newsId, commentId);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
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
        return commentRepository.findByNewsIdUseFilter(newsId, pageable, currentFilter);
    }

}
