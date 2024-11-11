package ru.clevertec.cache.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheTest {
    private LRUCache cache;

    @BeforeEach
    void setUp() {
        cache = new LRUCache(2, "cache");
    }

    @Test
    void get_ifNoValue() {
        assertNull(cache.get(1));
    }

    @Test
    void get_ifValuePresent() {
        cache.put(1, "1");

        Cache.ValueWrapper valueWrapper = cache.get(1);

        assertNotNull(valueWrapper);
        assertEquals("1", valueWrapper.get());
    }

    @Test
    void put_ifCacheIsNotFull() {
        cache.put(1, "1");

        assertTrue(cache.getCache().containsKey(1));
    }

    @Test
    void put_ifCacheIsFull() {
        cache.put(1, "1");
        cache.put(2, "2");
        cache.get(1);

        cache.put(3, "3");

        assertFalse(cache.getCache().containsKey(2));
        assertTrue(cache.getCache().containsKey(3));
        assertEquals(2, cache.getCache().size());
    }

    @Test
    void evict() {
        cache.put(1, "1");

        cache.evict(1);

        assertNull(cache.get(1));
    }

    @Test
    void clear() {
        cache.put(1, new Object());

        cache.clear();

        assertEquals(0, cache.getCache().size());
    }
}