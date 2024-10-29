package ru.clevertec.news.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NotNull(message = "CreateComment must not be null")
public class CreateComment {
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotBlank(message = "Text must not be blank")
    private String text;
}
