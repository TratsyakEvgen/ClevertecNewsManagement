package ru.clevertec.news.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableCaching
public class Config {
    @Bean
    @ConfigurationProperties(prefix = "cache")
    public ConfigCacheManager configCacheManager(){
        return new ConfigCacheManager();
    }

    @Bean
    public CacheManager cacheManager(ConfigCacheManager config) {
        return new AlgorithmCacheManager(config);
    }
}
