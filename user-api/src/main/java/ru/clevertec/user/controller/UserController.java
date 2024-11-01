package ru.clevertec.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.user.dto.ResponseUser;
import ru.clevertec.user.dto.request.CreateUser;
import ru.clevertec.user.service.UserService;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping(path = "/{username}", params = "password")
    public ResponseUser get(@PathVariable String username, String password) {
        return userService.get(username, password);
    }

    @PostMapping
    public ResponseUser create(@RequestBody CreateUser createUser) {
        return userService.create(createUser);
    }
}
