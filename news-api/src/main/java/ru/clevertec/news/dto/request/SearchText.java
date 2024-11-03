package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO поиска текста
 */
@Data
@NotNull(message = "SearchText must not be null")
public class SearchText {
    /**
     * Искомый текст
     */
    private String text;
}
