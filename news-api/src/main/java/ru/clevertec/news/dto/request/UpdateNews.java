package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO обновления новости
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull(message = "UpdateNews must not be null")
public class UpdateNews {
    /**
     * Текст заголовка
     */
    @NotBlank(message = "Title must not be blank")
    private String title;
    /**
     * Текст новости
     */
    @NotBlank(message = "Text must not be blank")
    private String text;
}
