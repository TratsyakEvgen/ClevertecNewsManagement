package ru.clevertec.news.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseComment {
    private long id;
    private String username;
    private LocalDateTime createAtDate;
    private String text;
    private long newsId;
}
