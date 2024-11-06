package ru.clevertec.news.unit.service.security.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.news.service.security.SecretKeyGenerator;
import ru.clevertec.news.service.security.impl.DefaultSecretKeyGenerator;

import static org.junit.jupiter.api.Assertions.*;

class DefaultSecretKeyGeneratorTest {

    private SecretKeyGenerator secretKeyGenerator;

    @BeforeEach
    void setUp() {
        secretKeyGenerator = new DefaultSecretKeyGenerator();
    }

    @Test
    void generate() {
        assertNotNull(secretKeyGenerator.generate());
    }
}