package ru.clevertec.news.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;

public interface CommentService {
    void delete(long newsId, long commentId);

    ResponseComment update(long newsId, long commentId, @Valid UpdateComment updateComment);

    ResponseComment create(long newsId, @Valid CreateComment createComment);

    ResponseComment get(long newsId, long commentId);

    Page<ResponseComment> get(long newsId, @NotNull(message = "Pageable must be not null") Pageable pageable,
                              @Valid SearchText searchText);

}
