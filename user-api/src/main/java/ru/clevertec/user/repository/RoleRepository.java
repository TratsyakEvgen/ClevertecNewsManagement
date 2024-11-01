package ru.clevertec.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.clevertec.user.entity.Role;
import ru.clevertec.user.entity.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(RoleName roleName);
}