package ru.clevertec.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;
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
    public Page<ResponseComment> search(@PathVariable long newsId, Pageable pageable, SearchText searchText) {
        return commentService.get(newsId, pageable, searchText);
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
