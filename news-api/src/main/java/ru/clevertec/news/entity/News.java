package ru.clevertec.news.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сущность новостей
 */
@Entity
@Getter
@Setter
@NotNull(message = "News must not be null")
public class News implements Serializable {
    /**
     * Идентификатор новости
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    @Min(value = 1, message = "Id must not be less than 1")
    private long id;
    /**
     * Имя пользователя (автора)
     */
    @NotBlank(message = "Username must not be blank")
    private String username;
    /**
     * Дата создания новости
     */
    @NotNull(message = "Date must not be null")
    private LocalDateTime date;
    /**
     * Заголовок новости
     */
    @NotBlank(message = "Text must not be blank")
    private String title;
    /**
     * Текст новости
     */
    @NotBlank(message = "Text must not be blank")
    private String text;
    /**
     * Список комментариев
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, mappedBy = "news")
    private List<Comment> comments;
}
