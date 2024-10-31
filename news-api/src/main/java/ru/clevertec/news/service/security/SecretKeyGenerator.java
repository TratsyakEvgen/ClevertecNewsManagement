package ru.clevertec.news.service.security;

import javax.crypto.SecretKey;

public interface SecretKeyGenerator {
    SecretKey generate();
}
