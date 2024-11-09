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
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.UpdateComment;
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
class CommentTest {
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
                Arguments.of("/news/1/comments", "0", "20"),
                Arguments.of("/news/1/comments?page=1", "1", "20"),
                Arguments.of("/news/1/comments?page=1&size=2", "1", "2"),
                Arguments.of("/news/1/comments?&size=2", "0", "2")
        );
    }

    static Stream<Arguments> getFullTextSearchRequest() {
        return Stream.of(
                Arguments.of("/news/1/comments", "user1", "1"),
                Arguments.of("/news/2/comments", "user1", "1"),
                Arguments.of("/news/1/comments", "need", "2")
        );
    }

    @AfterEach
    void clearCache() {
        Objects.requireNonNull(cacheManager.getCache("comments")).clear();
    }

    @Test
    void getComment() throws Exception {
        mockMvc.perform(get("/news/1/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.text").exists())
                .andExpect(jsonPath("$.newsId").value("1"));
    }

    @Test
    void getComment_ifCommentNotFound() throws Exception {
        mockMvc.perform(get("/news/2/comments/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.error")
                        .value("Comment with id 1 in news with id 2 not found"))
                .andExpect(jsonPath("$.path").value("/news/2/comments/1"));
    }

    @ParameterizedTest
    @MethodSource("getPageableRequest")
    void getAllComments_withPageable(String request, String number, String size) throws Exception {
        mockMvc.perform(get(request))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.number").value(number))
                .andExpect(jsonPath("$.size").value(size))
                .andExpect(jsonPath("$.totalElements").value("10"));
    }

    @ParameterizedTest
    @MethodSource("getFullTextSearchRequest")
    void getAllComments_fullTextSearch(String uri, String text, String totalElements) throws Exception {
        mockMvc.perform(get(uri)
                        .param("text", text))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.totalElements").value(totalElements));
    }

    @Test
    void createComment_ifNtAuthenticated() throws Exception {
        CreateComment createComment = new CreateComment("user1", "text");

        mockMvc.perform(post("/news/1/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createComment)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createComment_ifNotAuthorizedRole() throws Exception {
        CreateComment createComment = new CreateComment("user", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist", "JOURNALIST");

        mockMvc.perform(post("/news/1/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createComment))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void createComment_ifAdminRole() throws Exception {
        CreateComment createComment = new CreateComment("user", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "admin", "ADMIN");

        mockMvc.perform(post("/news/1/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createComment))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.newsId").value("1"));
    }

    @Test
    void createComment_ifSubscriberRole() throws Exception {
        CreateComment createComment = new CreateComment("user", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "SUBSCRIBER");

        mockMvc.perform(post("/news/1/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createComment))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").value("user"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.newsId").value("1"));
    }

    @Test
    void createComment_ifNotValidData() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "admin", "ADMIN");

        mockMvc.perform(post("/news/1/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new CreateComment()))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.error")
                        .value(allOf(
                                containsString("Text must not be blank"),
                                containsString("Username must not be blank")
                        )))
                .andExpect(jsonPath("$.path").value("/news/1/comments"));
    }

    @Test
    void updateComment_ifNotAuthenticated() throws Exception {
        mockMvc.perform(patch("/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UpdateComment("text"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateComment_ifNotAuthorizedRole() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist", "JOURNALIST");

        mockMvc.perform(patch("/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UpdateComment("text")))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateComment_ifAdminRole() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "admin", "ADMIN");

        mockMvc.perform(patch("/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UpdateComment("text")))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.newsId").value("1"));
    }

    @Test
    void updateComment_ifCommentCreatedByThisUser() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user1", "SUBSCRIBER");

        mockMvc.perform(patch("/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UpdateComment("text")))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.date").exists())
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.newsId").value("1"));
    }

    @Test
    void updateComment_ifCommentNotCreatedByThisUser() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user2", "SUBSCRIBER");

        mockMvc.perform(patch("/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UpdateComment("text")))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateComment_ifNotValidData() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "admin", "ADMIN");

        mockMvc.perform(patch("/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UpdateComment()))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.error")
                        .value(allOf(
                                containsString("Text must not be blank")
                        )))
                .andExpect(jsonPath("$.path").value("/news/1/comments/1"));
    }

    @Test
    void updateComment_ifCommentNotFound() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user2", "SUBSCRIBER");

        mockMvc.perform(patch("/news/2/comments/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UpdateComment("text")))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteComment_ifNotAuthenticated() throws Exception {
        mockMvc.perform(delete("/news/1/comments/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteComment_ifNotAuthorizedRole() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "journalist", "JOURNALIST");

        mockMvc.perform(delete("/news/1/comments/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteComment_ifAdminRole() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "admin", "ADMIN");

        mockMvc.perform(delete("/news/1/comments/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void deleteComment_ifCommentCreatedByThisUser() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user1", "SUBSCRIBER");

        mockMvc.perform(delete("/news/1/comments/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void deleteComment_ifCommentNotCreatedByThisUser() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user2", "SUBSCRIBER");

        mockMvc.perform(delete("/news/1/comments/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteComment_ifCommentNotFound() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user2", "SUBSCRIBER");

        mockMvc.perform(delete("/news/2/comments/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

}