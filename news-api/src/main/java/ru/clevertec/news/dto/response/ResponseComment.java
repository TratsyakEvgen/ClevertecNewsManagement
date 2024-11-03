package ru.clevertec.news.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO комментария
 */
@Data
public class ResponseComment {
    /**
     * Идентификатор комментария
     */
    private long id;
    /**
     * Имя пользователя (автора)
     */
    private String username;
    /**
     * Дата создания комментария
     */
    private LocalDateTime date;
    /**
     * Текст комментария
     */
    private String text;
    /**
     * Идентификатор новости
     */
    private long newsId;
}
