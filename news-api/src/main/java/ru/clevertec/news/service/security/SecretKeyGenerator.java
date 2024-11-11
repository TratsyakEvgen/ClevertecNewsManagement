package ru.clevertec.news.service.security;

import javax.crypto.SecretKey;

/**
 * Генератор секретных ключей
 */
public interface SecretKeyGenerator {
    /**
     * Генерирует секретный ключ
     *
     * @return секретный ключ
     */
    SecretKey generate();
}
