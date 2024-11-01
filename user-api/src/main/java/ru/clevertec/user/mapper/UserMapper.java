package ru.clevertec.user.mapper;

import org.mapstruct.*;
import ru.clevertec.user.dto.ResponseUser;
import ru.clevertec.user.dto.request.CreateUser;
import ru.clevertec.user.entity.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "roleName", source = "role.roleName")
    ResponseUser toResponseUser(User user);
    @Mapping(target = "role", source = "roleName")
    User toUser(CreateUser createUser);
}
