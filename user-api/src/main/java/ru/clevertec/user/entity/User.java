package ru.clevertec.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Сущность пользователей
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@Accessors(chain = true)
public class User {
    /**
     * Идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;
    /**
     * Имя пользователя
     */
    private String username;
    /**
     * Пароль пользователя
     */
    private String password;
    /**
     * Роль пользователя
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;
}

