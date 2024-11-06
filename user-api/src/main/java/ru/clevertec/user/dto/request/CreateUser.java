package ru.clevertec.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.clevertec.user.enums.RoleName;

/**
 * DTO для создания пользователя
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
