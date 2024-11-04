package ru.clevertec.news.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.cache.manager.AlgorithmCacheManager;
import ru.clevertec.cache.manager.ConfigurationCacheManager;


/**
 * Конфигурация LFU/LRU кэша
 */
@Configuration
public class AlgorithmCacheConfiguration {
    /**
     * @return конфигурация кэш менеджера.
     * Указывается алгоритм и максимальный размер кэша из файла свойств
     */
    @Bean
    @ConfigurationProperties(prefix = "cache")
    public ConfigurationCacheManager configCacheManager() {
        return new ConfigurationCacheManager();
    }

    /**
     * @param config конфигурация кэш менеджера
     * @return LFU/LRU кэш менеджер
     */
    @Bean
    public CacheManager cacheManager(ConfigurationCacheManager config) {
        return new AlgorithmCacheManager(config);
    }
}
