package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO обновления комментариев
 */
@Data
@NotNull(message = "UpdateComment must not be null")
public class UpdateComment {
    /**
     * Текст комментария
     */
    @NotBlank(message = "Text must not be blank")
    private String text;
}
