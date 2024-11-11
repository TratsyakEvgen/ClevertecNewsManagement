package ru.clevertec.news.integration.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ru.clevertec.cache.manager.AlgorithmCacheManager;
import ru.clevertec.cache.manager.ConfigurationCacheManager;


@TestConfiguration
@Profile("test")
public class AlgorithmLFUCacheConfiguration {
    @Bean
    public CacheManager cacheManager() {
        return new AlgorithmCacheManager(new ConfigurationCacheManager("LFU", 100));
    }
}
