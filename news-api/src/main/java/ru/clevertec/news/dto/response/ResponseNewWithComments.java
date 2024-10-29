package ru.clevertec.news.dto.response;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

@Data
public class ResponseNewWithComments {
    private long id;
    private String username;
    private LocalDateTime date;
    private String title;
    private String text;
    private Page<ResponseComment> comments;
}
