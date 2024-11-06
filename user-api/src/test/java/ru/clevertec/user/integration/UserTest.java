package ru.clevertec.user.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.user.dto.request.CreateUser;
import ru.clevertec.user.enums.RoleName;
import ru.clevertec.user.integration.container.PostgresTestContainer;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ImportTestcontainers(PostgresTestContainer.class)
public class UserTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void get_ifUserExists() throws Exception {
        mockMvc.perform(get("/users/user1?password=user1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.roleName").value("SUBSCRIBER"));
    }

    @Test
    void get_ifUserNotExists() throws Exception {
        mockMvc.perform(get("/users/user11?password=user1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.error").value("User with username user11 not found"))
                .andExpect(jsonPath("$.path").value("/users/user11"));
    }

    @Test
    void get_ifIncorrectPassword() throws Exception {
        mockMvc.perform(get("/users/user1?password=user2"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.error")
                        .value("User with username user1 and password *** not found"))
                .andExpect(jsonPath("$.path").value("/users/user1"));
    }


    @Test
    void create_ifNoValidRequest() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(new CreateUser())))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.error")
                        .value(allOf(
                                containsString("Role name must not be null"),
                                containsString("Password must not be blank"),
                                containsString("Username must not be blank")
                        )))
                .andExpect(jsonPath("$.path").value("/users"));
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(
                                new CreateUser("user123", "password", RoleName.SUBSCRIBER)
                        )))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.username").value("user123"))
                .andExpect(jsonPath("$.roleName").value("SUBSCRIBER"));
    }

    @Test
    void create_ifUserAlreadyExists() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(
                                new CreateUser("user1", "password", RoleName.SUBSCRIBER)
                        )))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.error")
                        .value("User with username user1 already exists"))
                .andExpect(jsonPath("$.path").value("/users"));;
    }


}
