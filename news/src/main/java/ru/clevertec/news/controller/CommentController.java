package ru.clevertec.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.Filter;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.dto.response.ResponsePage;
import ru.clevertec.news.service.CommentService;

@RestController
@RequestMapping("/news/{newsId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/{commentId}")
    public ResponseComment get(@PathVariable long newsId, @PathVariable long commentId) {
        return commentService.get(newsId, commentId);
    }

    @GetMapping
    public ResponsePage<ResponseComment> search(@PathVariable long newsId, Pageable pageable, Filter filter) {
        return commentService.get(newsId, pageable, filter);
    }

    @PostMapping
    public ResponseComment create(@PathVariable long newsId, @RequestBody CreateComment createComment) {
        return commentService.create(newsId, createComment);
    }

    @PatchMapping("/{commentId}")
    public ResponseComment update(@PathVariable long newsId,
                                  @PathVariable long commentId,
                                  @RequestBody UpdateComment updateComment) {
        return commentService.update(newsId, commentId, updateComment);
    }

    @DeleteMapping("/{commentId}")
    public void delete(@PathVariable long newsId, @PathVariable long commentId) {
        commentService.delete(newsId, commentId);
    }

}
