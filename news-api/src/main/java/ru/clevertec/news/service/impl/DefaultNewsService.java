package ru.clevertec.news.service.impl;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

/**
 * Сервис для управления новостями
 */
@Service
@RequiredArgsConstructor
@Transactional
@Validated
@Slf4j
public class DefaultNewsService implements NewsService {
    private final CacheableNewsService cacheableNewsService;
    private final CacheableCommentService cacheableCommentService;
    private final CommentService commentService;
    private final NewsMapper newsMapper;

    /**
     * Создание новости
     *
     * @param createNews информация о создании новости
     * @return новость
     * @throws ConstraintViolationException если createNews не валидный
     */
    @Override
    public ResponseNews create(CreateNews createNews) {
        log.debug("NewsService: create news " + createNews);

        News news = newsMapper.toNews(createNews);
        news.setDate(LocalDateTime.now());
        cacheableNewsService.save(news);
        ResponseNews responseNews = newsMapper.toResponseNews(news);

        log.debug(String.format("NewsService: create news %s, result %s", createNews, responseNews));
        return responseNews;
    }

    /**
     * Предоставляет страницу новостей по заданным параметрам (pageable, searchText)
     *
     * @param pageable   информация об пагинации
     * @param searchText искомый текст
     * @return страница новостей
     * @throws ConstraintViolationException если Pageable равен null или SearchText не валидный
     */
    @Override
    public Page<ResponseNews> get(Pageable pageable, SearchText searchText) {
        log.debug(String.format("NewsService: get news pageable %s, searchText %s", pageable, searchText));

        Page<News> newsPage = cacheableNewsService.findAll(pageable, searchText);
        Page<ResponseNews> responseNewsPage = newsPage.map(newsMapper::toResponseNews);

        log.debug(String.format(
                "NewsService: get news pageable %s, searchText %s, result %s", pageable, searchText, responseNewsPage
        ));
        return responseNewsPage;
    }

    /**
     * Обновляет новость.
     * Обновление подлежат поля переданные в updateNews (null поля не обновляются).
     * Перед изменениями новость удаляется из кэша,
     * так как в случае исключения при сохранении в кэше останутся не консистентные данные.
     * После сохранения данные заново кэшируются
     *
     * @param updateNews updateNews обновляемая информация
     * @param id         id новости
     * @return обновленная новость
     * @throws ConstraintViolationException если updateNews не валиден
     */
    @Override
    public ResponseNews update(UpdateNews updateNews, long id) {
        log.debug(String.format("NewsService: update news %d, update %s", id, updateNews));

        News news = cacheableNewsService.find(id);
        cacheableNewsService.evict(news);
        newsMapper.partialUpdate(updateNews, news);
        cacheableNewsService.save(news);
        ResponseNews responseNews = newsMapper.toResponseNews(news);

        log.debug(String.format("NewsService: update news %d, update %s, result %s", id, updateNews, responseNews));
        return responseNews;
    }

    /**
     * Удаление новости. После удаления новости удаляются все связанные кэшированные комментарии
     *
     * @param id id новости
     */
    @Override
    public void delete(long id) {
        log.debug("NewsService: delete news " + id);

        List<Comment> comments = cacheableNewsService.find(id).getComments();
        cacheableNewsService.delete(id);
        cacheableCommentService.evict(comments);
    }

    /**
     * Предоставляет новость со страницей комментариев
     *
     * @param id       id новости
     * @param pageable информация об пагинации комментариев
     * @return новость со страницей комментариев
     * @throws ConstraintViolationException если Pageable равен null
     */
    @Override
    public ResponseNewWithComments get(long id, Pageable pageable) {
        log.debug(String.format("NewsService: get news %d pageable %s", id, pageable));

        News news = cacheableNewsService.find(id);
        ResponseNewWithComments responseNewWithComments = newsMapper.toResponseNewWithComments(news);
        Page<ResponseComment> comments = commentService.get(id, pageable, new SearchText());
        responseNewWithComments.setComments(comments);

        log.debug(String.format("NewsService: get news %d pageable %s, result %s", id, pageable, responseNewWithComments));
        return responseNewWithComments;
    }


}
