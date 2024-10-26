package ru.clevertec.news.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class Config {
    @Bean
    public CacheManager cacheManager() {
        return new AlgorithmCacheManager("LFU", 20);
    }
}
