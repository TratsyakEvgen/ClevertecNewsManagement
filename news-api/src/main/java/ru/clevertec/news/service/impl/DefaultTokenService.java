package ru.clevertec.news.service.impl;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.news.dto.request.AuthenticationData;
import ru.clevertec.news.dto.response.ResponseToken;
import ru.clevertec.news.dto.response.ResponseUser;
import ru.clevertec.news.service.TokenService;
import ru.clevertec.news.service.UserClient;
import ru.clevertec.news.service.security.SecretKeyGenerator;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Сервис управления токенами доступа
 */
@Service
@RequiredArgsConstructor
@Validated
public class DefaultTokenService implements TokenService {
    private final SecretKeyGenerator secretKeyGenerator;
    private final UserClient userClient;
    @Value(value = "jwt.expiry-time")
    private String expiryTime;

    /**
     * Генерирует JWT токен на основании имени пользователя и роли полученной от userClient
     *
     * @param authenticationData информация для аутентификации пользователя
     */
    @Override
    public ResponseToken createToken(AuthenticationData authenticationData) {
        SecretKey secretKey = secretKeyGenerator.generate();
        String username = authenticationData.getUsername();
        ResponseUser user = userClient.getUserInfo(username, authenticationData.getPassword());

        String token = Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() + Integer.parseInt(expiryTime)))
                .signWith(secretKey)
                .subject(username)
                .claim("scope", user.getRoleName())
                .compact();
        return new ResponseToken(token);
    }
}
