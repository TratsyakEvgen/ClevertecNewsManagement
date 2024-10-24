package ru.clevertec.news.dto.request;

import lombok.Data;

@Data
public class CreateNews {
    private String username;
    private String title;
    private String text;
}
