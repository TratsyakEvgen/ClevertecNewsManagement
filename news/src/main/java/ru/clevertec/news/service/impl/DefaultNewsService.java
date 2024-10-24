package ru.clevertec.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;
import ru.clevertec.news.dto.response.ResponsePage;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.mapper.NewsMapper;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.service.NewsEntityService;
import ru.clevertec.news.service.NewsService;
import ru.clevertec.news.service.ServiceException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultNewsService implements NewsService {
    private final NewsEntityService newsEntityService;
    private final CommentService commentService;
    private final NewsMapper newsMapper;

    @Override
    public ResponseNews create(CreateNews createNews) {
        return Optional.ofNullable(createNews)
                .map(newsMapper::toNews)
                .map(news -> news.setLocalDateTime(LocalDateTime.now()))
                .map(newsEntityService::save)
                .map(newsMapper::toResponseNews)
                .orElseThrow(() -> new ServiceException("CreateUser must not be null"));
    }

    @Override
    public ResponsePage<ResponseNews> getAll(Pageable pageable, Filter filter) {
        return Optional.ofNullable(pageable)
                .map(p -> newsEntityService.findAll(p, filter))
                .map(newsMapper::toResponsePage)
                .orElseThrow(() -> new ServiceException("Pageable must not be null"));
    }

    @Override
    public ResponseNews update(UpdateNews updateNews, long id) {
        News news = newsEntityService.findById(id);

        return Optional.ofNullable(updateNews)
                .map(update -> newsMapper.partialUpdate(update, news))
                .map(newsEntityService::save)
                .map(newsMapper::toResponseNews)
                .orElseThrow(() -> new ServiceException("UpdateNews must not be null"));
    }

    @Override
    public void delete(long id) {
        newsEntityService.deleteById(id);
    }

    @Override
    public ResponseNewWithComments get(long id, Pageable pageable) {
        News news = newsEntityService.findById(id);
        ResponseNewWithComments responseNewWithComments = newsMapper.toResponseNewWithComments(news);
        ResponsePage<ResponseComment> comments = commentService.get(id, pageable, null);
        responseNewWithComments.setComments(comments);
        return responseNewWithComments;
    }


}
