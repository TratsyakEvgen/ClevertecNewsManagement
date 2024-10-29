package ru.clevertec.news.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.entity.News;

public interface CacheableNewsService {
    void evict(@Valid News news);

    void save(@Valid News news);

    Page<News> findAll(@NotNull(message = "Pageable must be not null") Pageable pageable,
                       @Valid SearchText searchText);

    News find(long id);

    void delete(long id);
}
