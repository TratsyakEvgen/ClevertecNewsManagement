package ru.clevertec.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.user.dto.ResponseUser;
import ru.clevertec.user.dto.request.CreateUser;
import ru.clevertec.user.service.UserService;

/**
 * Контролер пользователей
 */
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
    @PostMapping
    public ResponseUser create(@RequestBody CreateUser createUser) {
        return userService.create(createUser);
    }
}
