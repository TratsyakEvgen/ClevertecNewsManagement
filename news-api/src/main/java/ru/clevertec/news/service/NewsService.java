package ru.clevertec.news.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;

public interface NewsService {
    ResponseNews create(@Valid CreateNews createNews);

    Page<ResponseNews> getAll(@NotNull(message = "Pageable must be not null") Pageable pageable,
                              @Valid SearchText searchText);

    ResponseNews update(@Valid UpdateNews updateNews, long id);

    void delete(long id);

    ResponseNewWithComments get(long id, @NotNull(message = "Pageable must be not null") Pageable pageable);
}
