package ru.clevertec.user.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.user.controller.UserController;
import ru.clevertec.user.dto.ResponseUser;
import ru.clevertec.user.dto.request.CreateUser;
import ru.clevertec.user.enums.RoleName;
import ru.clevertec.user.service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    private UserController userController;
    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void get() {
        ResponseUser responseUser = new ResponseUser("user", RoleName.ADMIN);
        when(userService.get("user", "password")).thenReturn(responseUser);

        ResponseUser actual = userController.get("user", "password");

        assertEquals(responseUser, actual);
    }

    @Test
    void create() {
        CreateUser createUser = new CreateUser("user", "password", RoleName.ADMIN);
        ResponseUser responseUser = new ResponseUser("user", RoleName.ADMIN);
        when(userService.create(createUser)).thenReturn(responseUser);

        ResponseUser actual = userController.create(createUser);

        assertEquals(responseUser, actual);
    }
}