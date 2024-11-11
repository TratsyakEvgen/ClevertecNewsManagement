package ru.clevertec.news.integration.cache;

import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.test.context.ActiveProfiles;
import ru.clevertec.news.integration.container.PostgresTestContainer;
import ru.clevertec.news.integration.container.RedisTestContainer;

@ImportTestcontainers({PostgresTestContainer.class, RedisTestContainer.class})
@ActiveProfiles("test")
public class CommentRedisCacheTest extends CommentCacheTest {
}
