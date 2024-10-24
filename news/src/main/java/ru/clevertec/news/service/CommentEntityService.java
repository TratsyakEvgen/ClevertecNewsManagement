package ru.clevertec.news.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.entity.Comment;

public interface CommentEntityService {
    void delete(long newsId, long commentId);

    Comment save(Comment comment);

    Comment get(long newsId, long commentId);

    Page<Comment> get(long newsId, Pageable pageable, Filter filter);

}
