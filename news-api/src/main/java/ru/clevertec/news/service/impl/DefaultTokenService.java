package ru.clevertec.news.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.news.dto.request.AuthenticationData;
import ru.clevertec.news.dto.response.ResponseToken;
import ru.clevertec.news.dto.response.ResponseUser;
import ru.clevertec.news.service.TokenService;
import ru.clevertec.news.service.UserClient;
import ru.clevertec.news.service.security.SecretKeyGenerator;

import javax.crypto.SecretKey;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Validated
public class DefaultTokenService implements TokenService {
    private final SecretKeyGenerator secretKeyGenerator;
    private final UserClient userClient;

    @Override
    public ResponseToken createToken(AuthenticationData authenticationData) {
        SecretKey secretKey = secretKeyGenerator.generate();
        ResponseUser user = userClient.getUser(authenticationData.getUsername(), authenticationData.getPassword());

        String token = Jwts.builder()
//                .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(secretKey)
                .subject("user")
                .claim("scope", user.getRole())
                .compact();
        return new ResponseToken(token);
    }
}
