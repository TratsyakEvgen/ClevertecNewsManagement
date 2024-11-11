package ru.clevertec.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO токена доступа
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseToken {
    /**
     * Токен доступа
     */
    private String token;
}
