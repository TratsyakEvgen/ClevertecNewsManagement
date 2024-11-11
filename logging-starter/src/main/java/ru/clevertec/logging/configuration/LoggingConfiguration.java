package ru.clevertec.logging.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.logging.aspect.LogAspect;

/**
 * Конфигурация стартера
 */
@Configuration
public class LoggingConfiguration {

    /**
     * @return бин аспекта логирования, если данный бин не существует
     */
    @Bean
    @ConditionalOnMissingBean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
