package ru.clevertec.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.user.entity.Role;
import ru.clevertec.user.enums.RoleName;

import java.util.Optional;

/**
 * Репозиторий ролей
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Предоставляет роль по ее названию
     *
     * @param roleName наименование роли
     * @return роль
     */
    Optional<Role> findByRoleName(RoleName roleName);
}