package ru.clevertec.news.configuration;

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
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class DocumentationConfiguration {

    /**
     * @return добавляет заголовок в документацию
     */
    @Bean
    public OpenAPI titleOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("News API")
                        .version("0.0.1")
                );
    }
}
