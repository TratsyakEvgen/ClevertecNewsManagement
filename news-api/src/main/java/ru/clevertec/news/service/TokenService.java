package ru.clevertec.news.service;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.news.dto.request.AuthenticationData;
import ru.clevertec.news.dto.response.ResponseToken;

/**
 * Сервис для управления токенами доступа
 */
public interface TokenService {
    /**
     * Создает токен доступа
     *
     * @param authenticationData информация для аутентификации пользователя
     * @return токен доступа
     * @throws ConstraintViolationException если включена валидация в имплементации (присутствует {@link Validated}) и authenticationData валиден
     */
    ResponseToken createToken(@Valid AuthenticationData authenticationData);
}
