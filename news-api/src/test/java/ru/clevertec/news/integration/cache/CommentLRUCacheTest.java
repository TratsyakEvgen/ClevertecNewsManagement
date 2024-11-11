package ru.clevertec.news.integration.cache;

import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import ru.clevertec.news.integration.configuration.AlgorithmLRUCacheConfiguration;
import ru.clevertec.news.integration.container.PostgresTestContainer;

@ImportTestcontainers(PostgresTestContainer.class)
@ContextConfiguration(classes = AlgorithmLRUCacheConfiguration.class)
@ActiveProfiles("test")
public class CommentLRUCacheTest extends CommentCacheTest {
}
