package ru.clevertec.news.integration.e2e;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import ru.clevertec.exception.handler.starter.dto.ResponseError;
import ru.clevertec.news.dto.request.AuthenticationData;
import ru.clevertec.news.dto.response.ResponseToken;
import ru.clevertec.news.dto.response.ResponseUser;
import ru.clevertec.news.integration.configuration.RestTemplateConfiguration;
import ru.clevertec.news.integration.container.PostgresTestContainer;
import ru.clevertec.news.service.security.SecretKeyGenerator;
import ru.clevertec.news.util.JwtUtil;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = RestTemplateConfiguration.class)
@AutoConfigureMockMvc
@ImportTestcontainers(PostgresTestContainer.class)
@EnableWireMock(@ConfigureWireMock(port = 8081))
@ActiveProfiles("test")
class TokenTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SecretKeyGenerator keyGenerator;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RestTemplate restTemplate;
    @LocalServerPort
    private int port;

    @Test
    void createToken() throws Exception {
        AuthenticationData data = new AuthenticationData("user1", "password");
        WireMock.stubFor(WireMock.get("/users/user1?password=password")
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(new ResponseUser("SUBSCRIBER"))))
        );

        ResponseToken responseToken = restTemplate.postForObject("http://localhost:" + port + "/tokens", data, ResponseToken.class);

        assertNotNull(responseToken);
        Claims climes = JwtUtil.getClimes(keyGenerator.generate(), responseToken);
        assertEquals("user1", climes.get("sub"));
        assertEquals("SUBSCRIBER", climes.get("scope"));
    }

    @Test
    void createToken_ifUserNotFound() throws Exception {
        AuthenticationData data = new AuthenticationData("user1", "password");
        ResponseError responseError = new ResponseError().setError("User with username user1 not found");
        WireMock.stubFor(WireMock.get("/users/user1?password=password")
                .willReturn(WireMock.aResponse()
                        .withStatus(404)
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(responseError)))
        );

        mockMvc.perform(post("/tokens")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(data)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.error").value("User with username user1 not found"))
                .andExpect(jsonPath("$.path").value("/tokens"));
    }

    @Test
    void createToken_ifNotValidData() throws Exception {
        mockMvc.perform(post("/tokens")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new AuthenticationData())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.error")
                        .value(allOf(
                                containsString("Password must not be blank"),
                                containsString("Username must not be blank")
                        )))
                .andExpect(jsonPath("$.path").value("/tokens"));
    }
}