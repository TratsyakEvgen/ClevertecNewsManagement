package ru.clevertec.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Сущность ролей
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@Accessors(chain = true)
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
