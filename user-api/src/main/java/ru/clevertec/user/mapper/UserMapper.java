package ru.clevertec.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.clevertec.user.dto.ResponseUser;
import ru.clevertec.user.dto.request.CreateUser;
import ru.clevertec.user.entity.User;

/**
 * Конвертор пользователей
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    /**
     * Конвертирует сущность пользователя в его DTO
     *
     * @param user сущность пользователя
     * @return DTO пользователя
     */
    @Mapping(target = "roleName", source = "role.roleName")
    ResponseUser toResponseUser(User user);

    /**
     * Конвертирует DTO пользователя в его сущность
     *
     * @param createUser DTO пользователя сущность пользователя
     * @return сущность пользователя
     */
    @Mapping(target = "role", source = "roleName")
    User toUser(CreateUser createUser);
}
