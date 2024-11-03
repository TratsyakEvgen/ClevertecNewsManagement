package ru.clevertec.exception.handler.starter.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.exception.handler.starter.advice.GlobalExceptionHandler;

/**
 * Конфигурация стартера
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * @return конвертор json
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    /**
     * @param objectMapper конвертор json
     * @return бин аннотирований {@link RestControllerAdvice}, если данный бин не существует
     */
    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler(ObjectMapper objectMapper) {
        return new GlobalExceptionHandler(objectMapper);
    }
}
