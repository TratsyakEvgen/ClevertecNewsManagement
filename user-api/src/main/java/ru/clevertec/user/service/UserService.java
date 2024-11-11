package ru.clevertec.user.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.user.dto.ResponseUser;
import ru.clevertec.user.dto.request.CreateUser;

/**
 * Сервис для управления пользователями
 */
public interface UserService {
    /**
     * Возвращает пользователя по его имени и паролю
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return пользователь
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link Validated}
     *                                      и username или password равны null
     */
    ResponseUser get(@NotNull(message = "Username must not be null") String username,
                     @NotNull(message = "Password must not be null") String password);


    /**
     * Создает нового пользователя
     *
     * @param createUser информация о создании пользователя
     * @return созданный пользователь
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link Validated}
     *                                      и CreateUser не валидный
     */
    ResponseUser create(@Valid CreateUser createUser);
}
