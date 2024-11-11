package ru.clevertec.user.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурация Springdoc
 */
@Configuration
public class DocumentationConfiguration {

    /**
     * @return добавляет заголовок в документацию
     */
    @Bean
    public OpenAPI titleOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("User API")
                        .version("0.0.1")
                );
    }
}
