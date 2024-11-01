package ru.clevertec.user.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import ru.clevertec.user.dto.ResponseUser;
import ru.clevertec.user.dto.request.CreateUser;

public interface UserService {
    ResponseUser get(@NotNull(message = "Username must not be null") String username,
                     @NotNull(message = "Password must not be null") String password);

    ResponseUser create(@Valid CreateUser createUser);
}
