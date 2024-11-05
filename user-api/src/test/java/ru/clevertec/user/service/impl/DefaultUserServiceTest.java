package ru.clevertec.user.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.clevertec.exception.handler.starter.exception.EntityAlreadyExistsException;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;
import ru.clevertec.user.dto.ResponseUser;
import ru.clevertec.user.dto.request.CreateUser;
import ru.clevertec.user.entity.Role;
import ru.clevertec.user.entity.RoleName;
import ru.clevertec.user.entity.User;
import ru.clevertec.user.mapper.UserMapper;
import ru.clevertec.user.repository.RoleRepository;
import ru.clevertec.user.repository.UserRepository;
import ru.clevertec.user.service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userService = new DefaultUserService(userRepository, roleRepository, passwordEncoder, userMapper);
    }

    @Test
    void get() {
        User user = new User().setId(1);
        ResponseUser responseUser = new ResponseUser("user", RoleName.ADMIN);
        when(userRepository.findUserByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(userMapper.toResponseUser(user)).thenReturn(responseUser);

        ResponseUser actual = userService.get("user", "password");

        assertEquals(responseUser, actual);
    }

    @Test
    void get_ifUserNotPresent() {
        when(userRepository.findUserByUsername("user")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.get("user", "password"));
    }

    @Test
    void get_ifIncorrectPassword() {
        User user = new User();
        when(userRepository.findUserByUsername("user")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> userService.get("user", "password"));
    }

    @Test
    void create() {
        CreateUser createUser = new CreateUser("user", "password", RoleName.ADMIN);
        Role role = new Role().setRoleName(RoleName.ADMIN);
        User user = new User().setUsername("user")
                .setPassword("password")
                .setRole(role);
        ResponseUser responseUser = new ResponseUser("user", RoleName.ADMIN);
        when(userMapper.toUser(createUser)).thenReturn(user);
        when(userRepository.findUserByUsername("user")).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName(any())).thenReturn(Optional.of(role));
        when(userMapper.toResponseUser(user)).thenReturn(responseUser);

        ResponseUser actual = userService.create(createUser);

        assertEquals(responseUser, actual);
    }

    @Test
    void create_ifUserAlreadyExists() {
        CreateUser createUser = new CreateUser("user", "password", RoleName.ADMIN);
        Role role = new Role().setRoleName(RoleName.ADMIN);
        User user = new User().setUsername("user")
                .setPassword("password")
                .setRole(role);
        when(userMapper.toUser(createUser)).thenReturn(user);
        when(userRepository.findUserByUsername("user")).thenReturn(Optional.of(user));

        assertThrows(EntityAlreadyExistsException.class, () -> userService.create(createUser));
    }

    @Test
    void create_ifUserRoleNotExists() {
        CreateUser createUser = new CreateUser("user", "password", RoleName.ADMIN);
        User user = new User().setUsername("user");
        when(userMapper.toUser(createUser)).thenReturn(user);
        when(userRepository.findUserByUsername("user")).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName(any())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.create(createUser));
    }
}