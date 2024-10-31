package ru.clevertec.news.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "comments")
@NotNull(message = "Comment must not be null")
public class Comment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    @Min(value = 1, message = "Id must not be less than 1")
    private long id;
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotNull(message = "Date must not be null")
    private LocalDateTime date;
    @NotBlank(message = "Text must not be blank")
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;
}
