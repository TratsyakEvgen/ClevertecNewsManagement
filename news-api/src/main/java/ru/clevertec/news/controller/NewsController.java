package ru.clevertec.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.logging.annotation.Log;
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;
import ru.clevertec.news.service.NewsService;


@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    @Log
    @GetMapping
    public Page<ResponseNews> getAllNews(Pageable pageable, SearchText searchText) {
        return newsService.getAll(pageable, searchText);
    }
    @Log
    @GetMapping("/{id}")
    public ResponseNewWithComments getNews(@PathVariable long id, Pageable pageable) {
        return newsService.get(id, pageable);
    }
    @Log
    @PostMapping
    public ResponseNews createNews(@RequestBody CreateNews createNews) {
        return newsService.create(createNews);
    }
    @Log
    @PatchMapping("/{id}")
    public ResponseNews updateNews(@RequestBody UpdateNews updateNews, @PathVariable long id) {
        return newsService.update(updateNews, id);
    }
    @Log
    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable long id) {
        newsService.delete(id);
    }

}
