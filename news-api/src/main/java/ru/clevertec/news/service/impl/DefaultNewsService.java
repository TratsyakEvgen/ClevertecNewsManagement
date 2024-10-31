package ru.clevertec.news.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.mapper.NewsMapper;
import ru.clevertec.news.service.CacheableCommentService;
import ru.clevertec.news.service.CacheableNewsService;
import ru.clevertec.news.service.CommentService;
import ru.clevertec.news.service.NewsService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class DefaultNewsService implements NewsService {
    private final CacheableNewsService cacheableNewsService;
    private final CacheableCommentService cacheableCommentService;
    private final CommentService commentService;
    private final NewsMapper newsMapper;

    @Override
    public ResponseNews create(CreateNews createNews) {
        News news = newsMapper.toNews(createNews);
        news.setDate(LocalDateTime.now());
        cacheableNewsService.save(news);
        return newsMapper.toResponseNews(news);
    }

    @Override
    public Page<ResponseNews> getAll(Pageable pageable, SearchText searchText) {
        Page<News> newsPage = cacheableNewsService.findAll(pageable, searchText);
        return newsPage.map(newsMapper::toResponseNews);
    }

    @Override
    public ResponseNews update(UpdateNews updateNews, long id) {
        News news = cacheableNewsService.find(id);
        cacheableNewsService.evict(news);
        newsMapper.partialUpdate(updateNews, news);
        cacheableNewsService.save(news);
        return newsMapper.toResponseNews(news);
    }

    @Override
    public void delete(long id) {
        List<Comment> comments = cacheableNewsService.find(id).getComments();
        cacheableNewsService.delete(id);
        cacheableCommentService.evict(comments);
    }

    @Override
    public ResponseNewWithComments get(long id, Pageable pageable) {
        News news = cacheableNewsService.find(id);
        ResponseNewWithComments responseNewWithComments = newsMapper.toResponseNewWithComments(news);
        Page<ResponseComment> comments = commentService.get(id, pageable, new SearchText());
        responseNewWithComments.setComments(comments);
        return responseNewWithComments;
    }


}
