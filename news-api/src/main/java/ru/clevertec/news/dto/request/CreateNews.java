package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO создания новости
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@NotNull(message = "CreateNews must not be null")
public class CreateNews {
    /**
     * Имя пользователя (автора)
     */
    @NotBlank(message = "Username must not be blank")
    private String username;
    /**
     * Заголовок новости
     */
    @NotBlank(message = "Title must not be blank")
    private String title;
    /**
     * Текст новости
     */
    @NotBlank(message = "Text must not be blank")
    private String text;
}
