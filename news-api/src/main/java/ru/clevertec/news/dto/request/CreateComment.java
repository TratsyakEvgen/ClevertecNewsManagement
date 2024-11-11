package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO создания комментария
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull(message = "CreateComment must not be null")
public class CreateComment {
    /**
     * Имя пользователя (автора)
     */
    @NotBlank(message = "Username must not be blank")
    private String username;
    /**
     * Текст комментария
     */
    @NotBlank(message = "Text must not be blank")
    private String text;
}
