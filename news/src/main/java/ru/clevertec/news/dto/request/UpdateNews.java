package ru.clevertec.news.dto.request;

import lombok.Data;

@Data
public class UpdateNews {
    private String title;
    private String text;
}
