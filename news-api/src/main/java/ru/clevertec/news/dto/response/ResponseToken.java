package ru.clevertec.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO токена доступа
 */
@Data
@AllArgsConstructor
public class ResponseToken {
    /**
     * Токен доступа
     */
    private String token;
}
