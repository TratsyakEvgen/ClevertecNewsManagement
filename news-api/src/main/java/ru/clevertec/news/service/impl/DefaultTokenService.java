package ru.clevertec.news.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.news.dto.request.AuthenticationData;
import ru.clevertec.news.dto.response.ResponseToken;
import ru.clevertec.news.service.TokenService;
import ru.clevertec.news.service.security.SecretKeyGenerator;

import javax.crypto.SecretKey;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Validated
public class DefaultTokenService implements TokenService {
    private final SecretKeyGenerator secretKeyGenerator;

    @Override
    public ResponseToken createToken(AuthenticationData authenticationData) {
        SecretKey secretKey = secretKeyGenerator.generate();

        String token = Jwts.builder()
//                .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(secretKey)
                .subject("user")
                .claim("scope", "ADMIN")
                .compact();
        return new ResponseToken(token);
    }
}
