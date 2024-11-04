package ru.clevertec.cache.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AbstractAlgorithmCacheTest {
    private AbstractAlgorithmCache cache;

    @BeforeEach
    void setUp() {
        cache = new LFUCache(5, "cache");
    }

    @Test
    void getName() {
        assertEquals("cache", cache.getName());
    }

    @Test
    void getNativeCache() {
        assertThrows(UnsupportedOperationException.class, () -> cache.getNativeCache());
    }

    @Test
    void getByKeyAndClass() {
        assertThrows(UnsupportedOperationException.class, () -> cache.get(1, Integer.class));
    }

    @Test
    void getByKeyAndValueLoader() {
        assertThrows(UnsupportedOperationException.class, () -> cache.get(1, () -> null));
    }
}