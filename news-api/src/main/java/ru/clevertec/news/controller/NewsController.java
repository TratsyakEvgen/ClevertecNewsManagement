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
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.SearchText;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.dto.response.ResponseNewWithComments;
import ru.clevertec.news.dto.response.ResponseNews;
import ru.clevertec.news.service.NewsService;

/**
 * Контроллер новостей
 */
@ApiResponses(
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(schema = @Schema(implementation = ResponseError.class)))
)
@RestController
@RequestMapping("/news")
@RequiredArgsConstructor
public class NewsController {
    private final NewsService newsService;

    /**
     * Предоставляет страницу новостей по заданным параметрам (pageable, searchText)
     *
     * @param pageable   информация об пагинации
     * @param searchText искомый текст
     * @return страница новостей
     */
    @Operation(summary = "Get all news", tags = "news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
    })
    @Log
    @GetMapping
    public Page<ResponseNews> getAllNews(@ParameterObject Pageable pageable,
                                         @ParameterObject SearchText searchText) {
        return newsService.get(pageable, searchText);
    }

    /**
     * Предоставляет новость со страницей комментариев
     *
     * @param id       id новости
     * @param pageable информация об пагинации комментариев
     * @return новость со страницей комментариев
     */
    @Operation(summary = "Get news", tags = "news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "404", description = "News not found",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @Log
    @GetMapping("/{id}")
    public ResponseNewWithComments getNews(@PathVariable long id, @ParameterObject Pageable pageable) {
        return newsService.get(id, pageable);
    }

    /**
     * Создание новости
     *
     * @param createNews информация о создании новости
     * @return новость
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Create news", tags = "news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Incorrect request",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "422",
                    description = "No valid data (specific information is listed in the error field)",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @Log
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseNews createNews(@RequestBody CreateNews createNews) {
        return newsService.create(createNews);
    }

    /**
     * Обновление новости
     *
     * @param updateNews информация об обновлении новости
     * @param id         id новости
     * @return новость
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Update news", tags = "news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Incorrect request",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "News not found",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "422",
                    description = "No valid data (specific information is listed in the error field)",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @Log
    @PatchMapping("/{id}")
    public ResponseNews updateNews(@RequestBody UpdateNews updateNews, @PathVariable long id) {
        return newsService.update(updateNews, id);
    }

    /**
     * Удаление новости
     *
     * @param id id новости
     */
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Delete news", tags = "news")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Incorrect request",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "News not found",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @Log
    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable long id) {
        newsService.delete(id);
    }

}
