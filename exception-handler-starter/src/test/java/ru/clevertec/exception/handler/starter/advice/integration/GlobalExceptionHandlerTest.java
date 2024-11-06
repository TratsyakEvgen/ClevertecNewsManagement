package ru.clevertec.exception.handler.starter.advice.integration;

import feign.FeignException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.clevertec.exception.handler.starter.exception.EntityAlreadyExistsException;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;

import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = TestController.class)
class GlobalExceptionHandlerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TestService testService;

    @Test
    void handleConstraintViolationException() throws Exception {
        ConstraintViolation<?> mockConstraintViolation = mock(ConstraintViolation.class);
        doThrow(new ConstraintViolationException(Set.of(mockConstraintViolation)))
                .when(testService).doSome();
        when(mockConstraintViolation.getMessage()).thenReturn("Error validation");

        mockMvc.perform(get("/test"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("422"))
                .andExpect(jsonPath("$.error").value("Error validation"))
                .andExpect(jsonPath("$.path").value("/test"));

    }

    @Test
    void handleEntityNotFoundException() throws Exception {
        doThrow(new EntityNotFoundException("Entity not found")).when(testService).doSome();

        mockMvc.perform(get("/test"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.error").value("Entity not found"))
                .andExpect(jsonPath("$.path").value("/test"));
    }

    @Test
    void handleEntityAlreadyExistsException() throws Exception {
        doThrow(new EntityAlreadyExistsException("Entity already exists")).when(testService).doSome();

        mockMvc.perform(get("/test"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.error").value("Entity already exists"))
                .andExpect(jsonPath("$.path").value("/test"));
    }

    @Test
    void handleMethodArgumentTypeMismatchException() throws Exception {
        mockMvc.perform(get("/test/2s"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/test/2s"));
    }

    @Test
    void handleNoResourceFoundException() throws Exception {
        mockMvc.perform(get("/testsdfd"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.error").value("No static resource testsdfd."))
                .andExpect(jsonPath("$.path").value("/testsdfd"));
    }

    @Test
    void handleHttpMessageNotReadableException() throws Exception {
        mockMvc.perform(post("/test"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.path").value("/test"));
    }

    @Test
    void handleFeignExceptionNotFound() throws Exception {
        FeignException.NotFound mockFeignExceptionNotFound = mock(FeignException.NotFound.class);
        doThrow(mockFeignExceptionNotFound).when(testService).doSome();
        when(mockFeignExceptionNotFound.contentUTF8()).thenReturn("{\"error\": \"some\"}");

        mockMvc.perform(get("/test"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.error").value("some"))
                .andExpect(jsonPath("$.path").value("/test"));
    }

    @Test
    void handleUnknownException() throws Exception {
        doThrow(new NullPointerException("some")).when(testService).doSome();

        mockMvc.perform(get("/test"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value("500"))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.path").value("/test"));
    }
}