package ru.clevertec.user.integration.container;

import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Profile("test")
public class PostgresTestContainer {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("test")
            .withUsername("postgres")
            .withPassword("Traktaryst");


}
