package ru.clevertec.exception.handler.starter.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import ru.clevertec.exception.handler.starter.advice.GlobalExceptionHandler;

@org.springframework.context.annotation.Configuration
public class Configuration {
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
}
