package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO данных аутентификации
 */
@Data
@ToString(of = "username")
@NoArgsConstructor
@AllArgsConstructor
@NotNull(message = "AuthenticationData must not be null")
public class AuthenticationData {
    /**
     * Имя пользователя
     */

    @NotBlank(message = "Username must not be blank")
    private String username;
    /**
     * Пароль
     */
    @NotBlank(message = "Password must not be blank")
    private String password;
}
