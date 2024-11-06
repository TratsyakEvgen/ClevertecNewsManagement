package ru.clevertec.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.user.enums.RoleName;

/**
 * DTO пользователя
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
