package ru.clevertec.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Сущность ролей
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
public class Role {
    /**
     * Идентификатор роли
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long id;
    /**
     * Наименование роли
     */
    @Enumerated(EnumType.STRING)
    private RoleName roleName;
}
