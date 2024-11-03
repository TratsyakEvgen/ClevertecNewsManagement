package ru.clevertec.user.dto;

import lombok.Data;
import ru.clevertec.user.entity.RoleName;

/**
 * DTO пользователя
 */
@Data
public class ResponseUser {
    /**
     * Имя пользователя
     */
    private String username;
    /**
     * Роль пользователя
     */
    private RoleName roleName;
}
