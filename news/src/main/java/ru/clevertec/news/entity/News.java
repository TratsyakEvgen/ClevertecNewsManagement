package ru.clevertec.news.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "news_id")
    private long id;
    private String username;
    @Column(name = "date")
    private LocalDateTime localDateTime;
    private String title;
    private String text;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "news")
    private List<Comment> comments;
}
