package ru.clevertec.news.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.exception.handler.starter.dto.ResponseError;
import ru.clevertec.logging.annotation.Log;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.dto.response.ResponseComment;
import ru.clevertec.news.service.CommentService;

/**
 * Контроллер новостей
 */
@ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Incorrect request",
                content = @Content(schema = @Schema(implementation = ResponseError.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(schema = @Schema(implementation = ResponseError.class)))
})
@RestController
@RequestMapping("/news/{newsId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    /**
     * Предоставляет заданный комментарий для новости
     *
     * @param newsId    id новости
     * @param commentId id комментария
     * @return комментарий
     */
    @Operation(summary = "Get comment", tags = "comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "Comment for this news not found",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @Log
    @GetMapping(value = "/{commentId}")
    public ResponseComment getComment(@PathVariable long newsId, @PathVariable long commentId) {
        return commentService.get(newsId, commentId);
    }

    /**
     * Предоставляет страницу комментариев для новости по заданным параметрам (pageable, searchText)
     *
     * @param newsId     id новости
     * @param pageable   информация об пагинации
     * @param searchText искомый текст
     * @return страница комментариев
     */
    @Operation(summary = "Get all comments for news", tags = "comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "News not found",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @Log
    @GetMapping
    public Page<ResponseComment> getAllComments(@PathVariable long newsId,
                                                @ParameterObject Pageable pageable,
                                                @ParameterObject SearchText searchText) {
        return commentService.get(newsId, pageable, searchText);
    }

    /**
     * Создает комментарий для заданной новости
     *
     * @param newsId        id новости
     * @param createComment информация о создании комментария
     * @return комментарий
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create comment", tags = "comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "News not found",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "422",
                    description = "No valid data (specific information is listed in the error field)",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @Log
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseComment createComment(@PathVariable long newsId, @RequestBody CreateComment createComment) {
        return commentService.create(newsId, createComment);
    }

    /**
     * Обновляет заданный комментарий для новости
     *
     * @param newsId        id новости
     * @param commentId     id комментария
     * @param updateComment обновляемая информация
     * @return комментарий
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update comment", tags = "comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Comment for this news not found",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "422",
                    description = "No valid data (specific information is listed in the error field)",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @Log
    @PatchMapping(value = "/{commentId}")
    public ResponseComment updateComment(@PathVariable long newsId,
                                         @PathVariable long commentId,
                                         @RequestBody UpdateComment updateComment) {
        return commentService.update(newsId, commentId, updateComment);
    }

    /**
     * Удаляет заданный комментарий для новости
     *
     * @param newsId    id новости
     * @param commentId id комментария
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete comment", tags = "comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Comment for this news not found",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @Log
    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable long newsId, @PathVariable long commentId) {
        commentService.delete(newsId, commentId);
    }

}
