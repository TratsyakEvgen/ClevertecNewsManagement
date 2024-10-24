package ru.clevertec.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;
import ru.clevertec.news.dto.response.ResponsePage;
import ru.clevertec.news.service.NewsService;


@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @GetMapping
    public ResponsePage<ResponseNews> getAll(Pageable pageable, Filter filter) {
        return newsService.getAll(pageable, filter);
    }

    @GetMapping("/{id}")
    public ResponseNewWithComments get(@PathVariable long id, Pageable pageable) {
        return newsService.get(id, pageable);
    }

    @PostMapping
    public ResponseNews create(@RequestBody CreateNews createNews) {
        return newsService.create(createNews);
    }

    @PatchMapping("/{id}")
    public ResponseNews update(@RequestBody UpdateNews updateNews, @PathVariable long id) {
        return newsService.update(updateNews, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        newsService.delete(id);
    }

}
