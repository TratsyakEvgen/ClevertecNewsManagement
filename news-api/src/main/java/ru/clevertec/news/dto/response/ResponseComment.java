package ru.clevertec.news.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * DTO комментария
 */
@Data
@Accessors(chain = true)
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
