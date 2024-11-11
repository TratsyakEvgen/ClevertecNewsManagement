package ru.clevertec.news.integration.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.dto.request.CreateNews;
import ru.clevertec.news.dto.request.UpdateNews;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.integration.configuration.AlgorithmLFUCacheConfiguration;
import ru.clevertec.news.integration.container.PostgresTestContainer;
import ru.clevertec.news.service.security.SecretKeyGenerator;
import ru.clevertec.news.util.JwtUtil;

import java.util.Objects;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = AlgorithmLFUCacheConfiguration.class)
@AutoConfigureMockMvc
@ImportTestcontainers(PostgresTestContainer.class)
@Transactional
@ActiveProfiles("test")
class NewsTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SecretKeyGenerator keyGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CacheManager cacheManager;

    static Stream<Arguments> getPageableRequest() {
        return Stream.of(
                Arguments.of("/news", "0", "20"),
                Arguments.of("/news?page=1", "1", "20"),
                Arguments.of("/news?page=1&size=2", "1", "2"),
                Arguments.of("/news?&size=2", "0", "2")
        );
    }

    static Stream<Arguments> getFullTextSearchRequest() {
        return Stream.of(
                Arguments.of("/news", "journalist1", "5"),
                Arguments.of("/news", "journalist2", "5"),
                Arguments.of("/news", "british", "3")
        );
    }

    @AfterEach
    void clearCache() {
        Objects.requireNonNull(cacheManager.getCache("news")).clear();
    }

    @Test
    void getNews() throws Exception {
        mockMvc.perform(get("/news/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.username").value("journalist1"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.text").exists());
    }

    @Test
    void getNews_ifNewsNotFound() throws Exception {
        mockMvc.perform(get("/news/21"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.error")
                        .value("News with id 21 not found"))
                .andExpect(jsonPath("$.path").value("/news/21"));
    }

    @ParameterizedTest
    @MethodSource("getPageableRequest")
    void getAllNews_withPageable(String request, String number, String size) throws Exception {
        mockMvc.perform(get(request))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.number").value(number))
                .andExpect(jsonPath("$.size").value(size))
                .andExpect(jsonPath("$.totalElements").value("20"));
    }

    @ParameterizedTest
    @MethodSource("getFullTextSearchRequest")
    void getAllNews_fullTextSearch(String uri, String text, String totalElements) throws Exception {
        mockMvc.perform(get(uri)
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalElements").value(totalElements));
    }

    @Test
    void createNews_ifNotAuthenticated() throws Exception {
        CreateNews createNews = new CreateNews("journalist", "title", "text");

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createNews)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createNews_ifNotAuthorizedRole() throws Exception {
        CreateNews createNews = new CreateNews("user", "title", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "SUBSCRIBER");

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createNews))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void createNews_ifAdminRole() throws Exception {
        CreateNews createNews = new CreateNews("journalist", "title", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "admin", "ADMIN");

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createNews))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("journalist"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.text").value("text"));
    }

    @Test
    void createNews_ifJournalistRole() throws Exception {
        CreateNews createNews = new CreateNews("journalist", "title", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist", "JOURNALIST");

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createNews))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("journalist"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.text").value("text"));
    }

    @Test
    void createNews_ifNotValidData() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist", "JOURNALIST");

        mockMvc.perform(post("/news")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new News()))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.error")
                        .value(allOf(
                                containsString("Title must not be blank"),
                                containsString("Text must not be blank"),
                                containsString("Username must not be blank")
                        )))
                .andExpect(jsonPath("$.path").value("/news"));
    }

    @Test
    void updateNews_ifNotAuthenticated() throws Exception {
        UpdateNews updateNews = new UpdateNews("title", "text");

        mockMvc.perform(patch("/news/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateNews)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateNews_ifNotAuthorizedRole() throws Exception {
        UpdateNews updateNews = new UpdateNews("title", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "SUBSCRIBER");

        mockMvc.perform(patch("/news/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateNews))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateNews_ifAdminRole() throws Exception {
        UpdateNews updateNews = new UpdateNews("title", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "admin", "ADMIN");

        mockMvc.perform(patch("/news/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateNews))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("journalist1"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.text").value("text"));
    }

    @Test
    void updateNews_iNewsCreatedByThisUser() throws Exception {
        UpdateNews updateNews = new UpdateNews("title", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist1", "JOURNALIST");

        mockMvc.perform(patch("/news/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateNews))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("journalist1"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.text").value("text"));
    }

    @Test
    void updateNews_iNewsNotCreatedByThisUser() throws Exception {
        UpdateNews updateNews = new UpdateNews("title", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist2", "JOURNALIST");

        mockMvc.perform(patch("/news/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateNews))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateNews_ifNewsNotFound() throws Exception {
        UpdateNews updateNews = new UpdateNews("title", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist1", "JOURNALIST");

        mockMvc.perform(patch("/news/21")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateNews))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateNews_ifNotValidData() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist1", "JOURNALIST");

        mockMvc.perform(patch("/news/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UpdateNews()))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.error")
                        .value(allOf(
                                containsString("Title must not be blank"),
                                containsString("Text must not be blank")
                        )))
                .andExpect(jsonPath("$.path").value("/news/1"));
    }

    @Test
    void deleteNews_ifNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/news/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteNews_ifNotAuthorizedRole() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "SUBSCRIBER");

        mockMvc.perform(delete("/news/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteNews_ifAdminRole() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "admin", "ADMIN");

        mockMvc.perform(delete("/news/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void deleteNews_ifNewsCreatedByThisUser() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist1", "JOURNALIST");

        mockMvc.perform(delete("/news/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void deleteNews_ifCommentNotCreatedByThisUser() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist2", "JOURNALIST");

        mockMvc.perform(delete("/news/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteNews_ifNewsNotFound() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist1", "JOURNALIST");

        mockMvc.perform(delete("/news/21")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}