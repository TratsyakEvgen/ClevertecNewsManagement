
package ru.clevertec.news.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.cache.manager.AlgorithmCacheManager;
import ru.clevertec.cache.manager.ConfigurationCacheManager;


@Configuration
@EnableCaching
public class AlgorithmCacheConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "cache")
    public ConfigurationCacheManager configCacheManager() {
        return new ConfigurationCacheManager();
    }

    @Bean
    public CacheManager cacheManager(ConfigurationCacheManager config) {
        return new AlgorithmCacheManager(config);
    }
}
