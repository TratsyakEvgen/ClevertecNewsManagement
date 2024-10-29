package ru.clevertec.news.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.entity.Comment;

public interface CacheableCommentService {
    void evict(@Valid Comment comment);

    void delete(long newsId, long commentId);

    void save(@Valid Comment comment);

    Comment get(long newsId, long commentId);

    Page<Comment> findAll(long newsId, @NotNull(message = "Pageable must be not null") Pageable pageable,
                          @Valid SearchText searchText);
}
