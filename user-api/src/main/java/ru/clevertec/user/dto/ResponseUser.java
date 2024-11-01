package ru.clevertec.user.dto;

import lombok.Data;
import ru.clevertec.user.entity.RoleName;

@Data
public class ResponseUser {
    private String username;
    private RoleName roleName;
}
