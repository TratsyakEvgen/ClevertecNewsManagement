package ru.clevertec.news.dto.request;

import lombok.Data;

@Data
public class CreateComment {
    private String username;
    private String text;
}
