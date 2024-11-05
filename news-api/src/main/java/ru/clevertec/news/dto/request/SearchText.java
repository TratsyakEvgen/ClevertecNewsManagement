package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO поиска текста
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@NotNull(message = "SearchText must not be null")
public class SearchText {
    /**
     * Искомый текст
     */
    private String text;
}
