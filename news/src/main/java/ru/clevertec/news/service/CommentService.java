package ru.clevertec.news.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.dto.response.ResponsePage;

public interface CommentService {
    void delete(long newsId, long commentId);

    ResponseComment update(long newsId, long commentId, UpdateComment updateComment);

    ResponseComment create(long newsId, CreateComment createComment);

    ResponseComment get(long newsId, long commentId);

    ResponsePage<ResponseComment> get(long newsId, Pageable pageable, Filter filter);

}
