package ru.clevertec.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.logging.annotation.Log;
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

    @Log
    @GetMapping("/{commentId}")
    public ResponseComment getComment(@PathVariable long newsId, @PathVariable long commentId) {
        return commentService.get(newsId, commentId);
    }
    @Log
    @GetMapping
    public Page<ResponseComment> getAllComments(@PathVariable long newsId, Pageable pageable, SearchText searchText) {
        return commentService.get(newsId, pageable, searchText);
    }
    @Log
    @PostMapping
    public ResponseComment createComment(@PathVariable long newsId, @RequestBody CreateComment createComment) {
        return commentService.create(newsId, createComment);
    }
    @Log
    @PatchMapping("/{commentId}")
    public ResponseComment updateComment(@PathVariable long newsId,
                                  @PathVariable long commentId,
                                  @RequestBody UpdateComment updateComment) {
        return commentService.update(newsId, commentId, updateComment);
    }
    @Log
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable long newsId, @PathVariable long commentId) {
        commentService.delete(newsId, commentId);
    }

}
