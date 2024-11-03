package ru.clevertec.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.clevertec.user.entity.RoleName;

/**
 * DTO для создания пользователя
 */
@Data
@NotNull(message = "CreateUser name must not be null")
public class CreateUser {
    /**
     * Имя пользователя
     */
    @NotBlank(message = "Username must not be blank")
    private String username;
    /**
     * Пароль пользователя
     */
    @NotBlank(message = "Password must not be blank")
    private String password;
    /**
     * Роль пользователя
     */
    @NotNull(message = "Role name must not be null")
    private RoleName roleName;
}
