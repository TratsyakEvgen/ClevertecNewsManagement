package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO обновления комментариев
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull(message = "UpdateComment must not be null")
public class UpdateComment {
    /**
     * Текст комментария
     */
    @NotBlank(message = "Text must not be blank")
    private String text;
}
