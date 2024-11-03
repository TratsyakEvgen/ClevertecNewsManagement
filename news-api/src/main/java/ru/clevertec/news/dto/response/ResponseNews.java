package ru.clevertec.news.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO новости
 */
@Data
public class ResponseNews {
    /**
     * Идентификатор новости
     */
    private long id;
    /**
     * Имя пользователя (автора)
     */
    private String username;
    /**
     * Дата создания новости
     */
    private LocalDateTime date;
    /**
     * Заголовок новости
     */
    private String title;
    /**
     * Текст новости
     */
    private String text;
}
