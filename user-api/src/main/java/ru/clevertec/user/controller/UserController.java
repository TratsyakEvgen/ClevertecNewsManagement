package ru.clevertec.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.exception.handler.starter.dto.ResponseError;
import ru.clevertec.user.dto.ResponseUser;
import ru.clevertec.user.dto.request.CreateUser;
import ru.clevertec.user.service.UserService;

/**
 * Контролер пользователей
 */
@ApiResponses(value = {
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
                content = @Content(schema = @Schema(implementation = ResponseError.class)))
})
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Возвращает пользователя по его имени и паролю
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return пользователь
     */
    @Operation(summary = "Get user info", tags = "users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "Incorrect request",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "404", description = "User with username not found or incorrect password",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @GetMapping(path = "/{username}", params = "password")
    public ResponseUser get(@PathVariable String username, String password) {
        return userService.get(username, password);
    }

    /**
     * Создает нового пользователя
     *
     * @param createUser информация о создании пользователя
     * @return созданный пользователь
     */
    @Operation(summary = "Registry new user", tags = "users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "User already exits",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "404", description = "User with username not found or incorrect password",
                    content = @Content(schema = @Schema(implementation = ResponseError.class))),
            @ApiResponse(responseCode = "422",
                    description = "No valid data (specific information is listed in the error field)",
                    content = @Content(schema = @Schema(implementation = ResponseError.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseUser create(@RequestBody CreateUser createUser) {
        return userService.create(createUser);
    }
}
