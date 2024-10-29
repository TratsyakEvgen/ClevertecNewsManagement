package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull(message = "SearchText must not be null")
public class SearchText {
    private String text;
}
