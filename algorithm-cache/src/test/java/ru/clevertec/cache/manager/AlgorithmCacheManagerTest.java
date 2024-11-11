package ru.clevertec.cache.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import ru.clevertec.cache.cache.LFUCache;
import ru.clevertec.cache.exception.IncorrectAlgorithmException;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmCacheManagerTest {

    private AlgorithmCacheManager cacheManager;

    @BeforeEach
    void setUp() {
        cacheManager = new AlgorithmCacheManager(new ConfigurationCacheManager("LFU", 2));
    }

    @Test
    void getCache_ifCacheIsPresent() {
        Cache cache = new LFUCache(2, "cache");
        cacheManager.getCacheMap().put("cache", cache);

        assertEquals(cache, cacheManager.getCache("cache"));
    }

    @Test
    void getCache_ifCacheIsNotPresent() {
        cacheManager.getCache("cache");

        Cache cache = cacheManager.getCache("cache");

        assertNotNull(cache);
        assertEquals("cache", cache.getName());
    }

    @Test
    void getCache_createCacheWithIncorrectAlgorithm() {
        ConfigurationCacheManager configurationCacheManager = new ConfigurationCacheManager("Algorithm", 2);

        assertThrows(IncorrectAlgorithmException.class, () -> new AlgorithmCacheManager(configurationCacheManager).getCache("cache"));
    }

    @Test
    void getCacheNames() {
        cacheManager.getCache("cache");

        Collection<String> cacheNames = cacheManager.getCacheNames();

        assertArrayEquals(new String[]{"cache"}, cacheNames.toArray());
    }


}