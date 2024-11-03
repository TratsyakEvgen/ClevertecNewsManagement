package ru.clevertec.user.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.user.entity.User;

import java.util.Optional;

/**
 * Репозиторий пользователей
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Предоставляет пользователя с его ролью по его имени
     *
     * @param username имя пользователя
     * @return пользователь с ролью
     */
    @EntityGraph(attributePaths = "role")
    Optional<User> findUserByUsername(String username);
}