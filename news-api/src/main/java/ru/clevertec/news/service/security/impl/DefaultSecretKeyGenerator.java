package ru.clevertec.news.service.security.impl;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import ru.clevertec.news.service.security.SecretKeyGenerator;

import javax.crypto.SecretKey;

@Component
public class DefaultSecretKeyGenerator implements SecretKeyGenerator {
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();
    @Override
    public SecretKey generate() {
        return secretKey;
    }
}
