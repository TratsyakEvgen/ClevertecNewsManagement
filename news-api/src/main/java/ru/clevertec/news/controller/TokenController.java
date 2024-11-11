package ru.clevertec.news.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.exception.handler.starter.dto.ResponseError;
import ru.clevertec.logging.annotation.Log;
import ru.clevertec.news.dto.request.AuthenticationData;
import ru.clevertec.news.dto.response.ResponseToken;
import ru.clevertec.news.service.TokenService;

/**
 * Контроллер токенов доступа
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/tokens")
public class TokenController {
    private final TokenService tokenService;

    /**
     * Создание токена доступа
     *
     * @param authenticationData данные аутентификации
     * @return токен доступа
     */
    @Operation(summary = "Create new JWT token", tags = "tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "422",
                    description = "No valid data (specific information is listed in the error field)",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @Log
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseToken createToken(@RequestBody AuthenticationData authenticationData) {
        return tokenService.createToken(authenticationData);
    }
}
