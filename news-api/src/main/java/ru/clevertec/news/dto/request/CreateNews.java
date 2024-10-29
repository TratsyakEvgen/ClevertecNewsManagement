package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull(message = "CreateNews must not be null")
public class CreateNews {
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotBlank(message = "Title must not be blank")
    private String title;
    @NotBlank(message = "Text must not be blank")
    private String text;
}
