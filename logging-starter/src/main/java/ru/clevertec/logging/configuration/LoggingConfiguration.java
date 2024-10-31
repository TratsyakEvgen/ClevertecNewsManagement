package ru.clevertec.logging.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.clevertec.logging.aspect.LogAspect;

@Configuration
public class LoggingConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public LogAspect logAspect() {
        return new LogAspect();
    }
}
