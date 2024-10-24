package ru.clevertec.news.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseNews {
    private long id;
    private String username;
    private LocalDateTime localDateTime;
    private String title;
    private String text;
}
