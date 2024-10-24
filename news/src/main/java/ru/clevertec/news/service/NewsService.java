package ru.clevertec.news.service;

import org.springframework.data.domain.Pageable;
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;
import ru.clevertec.news.dto.response.ResponsePage;

public interface NewsService {
    ResponseNews create(CreateNews createNews);

    ResponsePage<ResponseNews> getAll(Pageable pageable, Filter filter);

    ResponseNews update(UpdateNews updateNews, long id);

    void delete(long id);

    ResponseNewWithComments get(long id, Pageable pageable);
}
