package ru.clevertec.user.service.impl;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
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

@Service
@RequiredArgsConstructor
@Transactional
@Validated
public class DefaultUserService implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    /**
     * Возвращает пользователя по его имени и паролю
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return пользователь
     * @throws ConstraintViolationException если username или password равны null
     * @throws EntityNotFoundException      если пользователя с username не существует или передан неверный пароль
     */
    @Override
    public ResponseUser get(String username, String password) {
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() ->
                        new EntityNotFoundException(String.format("User with username %s not found", username))
                );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new EntityNotFoundException(String.format("User with username %s and password *** not found", username));
        }
        return userMapper.toResponseUser(user);
    }

    /**
     * Создает нового пользователя
     *
     * @param createUser информация о создании пользователя
     * @return созданный пользователь
     * @throws ConstraintViolationException если CreateUser не валидный
     * @throws EntityAlreadyExistsException если пользователь с таким именем уже существует
     * @throws EntityNotFoundException      если указанная роль не найдена
     */
    @Override
    public ResponseUser create(CreateUser createUser) {
        User user = userMapper.toUser(createUser);
        String username = user.getUsername();

        userRepository.findUserByUsername(username)
                .ifPresent(u -> {
                    throw new EntityAlreadyExistsException("User with username " + username + " already exists");
                });

        RoleName roleName = createUser.getRoleName();
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Role %s not found", roleName)));


        user.setRole(role);
        String encodePassword = passwordEncoder.encode(createUser.getPassword());
        user.setPassword(encodePassword);
        userRepository.save(user);
        return userMapper.toResponseUser(user);
    }
}
