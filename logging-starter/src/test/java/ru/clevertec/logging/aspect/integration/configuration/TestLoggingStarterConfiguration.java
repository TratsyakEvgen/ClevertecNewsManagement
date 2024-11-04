package ru.clevertec.logging.aspect.integration.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import ru.clevertec.logging.aspect.integration.TestClass;

@TestConfiguration
public class TestLoggingStarterConfiguration {
    @Bean
    public TestClass testClass() {
        return new TestClass();
    }
}
