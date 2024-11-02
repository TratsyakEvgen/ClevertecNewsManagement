package ru.clevertec.news.service.security.impl;

import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;
import ru.clevertec.news.service.security.SecretKeyGenerator;

import javax.crypto.SecretKey;

/**
 * Генератор HS256 секретных ключей
 */
@Component
public class DefaultSecretKeyGenerator implements SecretKeyGenerator {
    private final SecretKey secretKey = Jwts.SIG.HS256.key().build();

    /**
     * Генерирует секретный ключ
     *
     * @return секретный ключ
     */
    @Override
    public SecretKey generate() {
        return secretKey;
    }
}
