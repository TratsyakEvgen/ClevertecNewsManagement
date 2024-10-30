package ru.clevertec.news.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NotNull(message = "News must not be null")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    @Min(value = 1, message = "Id must not be less than 1")
    private long id;
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotNull(message = "Date must not be null")
    private LocalDateTime date;
    @NotBlank(message = "Text must not be blank")
    private String title;
    @NotBlank(message = "Text must not be blank")
    private String text;
}
