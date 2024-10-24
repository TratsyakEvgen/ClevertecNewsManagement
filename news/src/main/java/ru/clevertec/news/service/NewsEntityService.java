package ru.clevertec.news.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.entity.News;

public interface NewsEntityService {
    News save(News news);

    Page<News> findAll(Pageable pageable, Filter filter);

    News findById(long id);

    void deleteById(long id);
}
