package ru.clevertec.news.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.news.dto.request.CreateComment;
import ru.clevertec.news.dto.request.UpdateComment;
import ru.clevertec.news.integration.container.PostgresTestContainer;
import ru.clevertec.news.integration.util.JwtUtil;
import ru.clevertec.news.service.security.SecretKeyGenerator;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ImportTestcontainers(PostgresTestContainer.class)
//@EnableWireMock(@ConfigureWireMock(port = 8081, name = "users"))
@Transactional
class CommentTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SecretKeyGenerator keyGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CacheManager cacheManager;


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
    void createComment_notAuthenticated() throws Exception {
        CreateComment createComment = new CreateComment("user1", "text");

        mockMvc.perform(post("/news/1/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createComment)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createComment_notAuthorizedRole() throws Exception {
        CreateComment createComment = new CreateComment("user", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "JOURNALIST");

        mockMvc.perform(post("/news/1/comments")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createComment))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void createComment_adminRole() throws Exception {
        CreateComment createComment = new CreateComment("user", "text");
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "ADMIN");

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
    void createComment_subscriberRole() throws Exception {
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
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "ADMIN");

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
    void updateComment_notAuthenticated() throws Exception {
        mockMvc.perform(patch("/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UpdateComment("text"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateComment_notAuthorizedRole() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "JOURNALIST");

        mockMvc.perform(patch("/news/1/comments/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new UpdateComment("text")))
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateComment_adminRole() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "ADMIN");

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
    void updateComment_subscriberRole() throws Exception {
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
    void updateComment_ifNotValidData() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "ADMIN");

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
    void deleteComment_notAuthenticated() throws Exception {
        mockMvc.perform(delete("/news/1/comments/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteComment_notAuthorizedRole() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "JOURNALIST");

        mockMvc.perform(delete("/news/1/comments/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteComment_adminRole() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user", "ADMIN");

        mockMvc.perform(delete("/news/1/comments/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


    @Test
    void deleteComment_subscriberRole() throws Exception {
        String token = JwtUtil.createToken(keyGenerator.generate(), "user1", "SUBSCRIBER");

        mockMvc.perform(delete("/news/1/comments/1")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }


    public static Stream<Arguments> getPageableRequest() {
        return Stream.of(
                Arguments.of("/news/1/comments", "0", "20"),
                Arguments.of("/news/1/comments?page=1", "1", "20"),
                Arguments.of("/news/1/comments?page=1&size=2", "1", "2"),
                Arguments.of("/news/1/comments?&size=2", "0", "2")
        );
    }

    public static Stream<Arguments> getFullTextSearchRequest() {
        return Stream.of(
                Arguments.of("/news/1/comments", "user1", "1"),
                Arguments.of("/news/2/comments", "user1", "1"),
                Arguments.of("/news/4/comments", "ago", "1"),
                Arguments.of("/news/5/comments", "american", "1")
        );
    }


}