package ru.clevertec.news.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

/**
 * DTO новости со страницей комментариев
 */
@Data
@Accessors(chain = true)
public class ResponseNewWithComments {
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
    /**
     * Страница DTO комментариев
     */
    private Page<ResponseComment> comments;
}
