package ru.clevertec.news.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUser {
    /**
     * Роль пользователя
     */
    private String roleName;
}
