package ru.clevertec.news.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Сущность комментария
 */
@Entity
@Getter
@Setter
@Table(name = "comments")
@NotNull(message = "Comment must not be null")
@Accessors(chain = true)
public class Comment implements Serializable {
    /**
     * Идентификатор комментария
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @Min(value = 1, message = "Id must not be less than 1")
    private long id;
    /**
     * Имя пользователя (автора)
     */
    @NotBlank(message = "Username must not be blank")
    private String username;
    /**
     * Дата создания комментария
     */
    @NotNull(message = "Date must not be null")
    private LocalDateTime date;
    /**
     * Текст комментария
     */
    @NotBlank(message = "Text must not be blank")
    private String text;
    /**
     * Новость к которой относиться комментарий
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;
}
