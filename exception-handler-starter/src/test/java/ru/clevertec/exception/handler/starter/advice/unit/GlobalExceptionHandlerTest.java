package ru.clevertec.exception.handler.starter.advice.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import ru.clevertec.exception.handler.starter.advice.GlobalExceptionHandler;
import ru.clevertec.exception.handler.starter.dto.ResponseError;
import ru.clevertec.exception.handler.starter.exception.EntityAlreadyExistsException;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
    private static final String URI = "uri";
    private GlobalExceptionHandler handler;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler(objectMapper);
    }

    @Test
    void handleConstraintViolationException() {
        ConstraintViolation<?> mockConstraintViolation = mock(ConstraintViolation.class);
        ConstraintViolationException exception = new ConstraintViolationException(Set.of(mockConstraintViolation));
        when(mockConstraintViolation.getMessage()).thenReturn("Error validation");
        when(httpServletRequest.getRequestURI()).thenReturn(URI);
        ResponseError expected = new ResponseError()
                .setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .setError("Error validation")
                .setPath(URI);

        ResponseError responseError = handler.handleConstraintViolationException(httpServletRequest, exception);

        assertEquals(expected.getPath(), responseError.getPath());
        assertEquals(expected.getError(), responseError.getError());
        assertEquals(expected.getStatus(), responseError.getStatus());
    }

    @Test
    void handleEntityNotFoundException() {
        EntityNotFoundException exception = new EntityNotFoundException("some");
        when(httpServletRequest.getRequestURI()).thenReturn(URI);
        ResponseError expected = new ResponseError()
                .setStatus(HttpStatus.NOT_FOUND.value())
                .setError(exception.getMessage())
                .setPath(URI);

        ResponseError responseError = handler.handleEntityNotFoundException(httpServletRequest, exception);

        assertEquals(expected.getPath(), responseError.getPath());
        assertEquals(expected.getError(), responseError.getError());
        assertEquals(expected.getStatus(), responseError.getStatus());
    }

    @Test
    void handleEntityAlreadyExistsException() {
        EntityAlreadyExistsException exception = new EntityAlreadyExistsException("some");
        when(httpServletRequest.getRequestURI()).thenReturn(URI);
        ResponseError expected = new ResponseError()
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setError(exception.getMessage())
                .setPath(URI);

        ResponseError responseError = handler.handleEntityAlreadyExistsException(httpServletRequest, exception);

        assertEquals(expected.getPath(), responseError.getPath());
        assertEquals(expected.getError(), responseError.getError());
        assertEquals(expected.getStatus(), responseError.getStatus());
    }

    @Test
    void handleMethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException mockMethodArgumentTypeMismatchException = mock(MethodArgumentTypeMismatchException.class);
        when(mockMethodArgumentTypeMismatchException.getMessage()).thenReturn("some");
        when(httpServletRequest.getRequestURI()).thenReturn(URI);
        ResponseError expected = new ResponseError()
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setError("some")
                .setPath(URI);

        ResponseError responseError = handler.handleMethodArgumentTypeMismatchException(
                httpServletRequest,
                mockMethodArgumentTypeMismatchException
        );

        assertEquals(expected.getPath(), responseError.getPath());
        assertEquals(expected.getError(), responseError.getError());
        assertEquals(expected.getStatus(), responseError.getStatus());
    }

    @Test
    void handleNoResourceFoundException() {
        when(httpServletRequest.getRequestURI()).thenReturn(URI);
        ResponseError expected = new ResponseError()
                .setStatus(HttpStatus.BAD_REQUEST.value())
                .setError("No static resource some.")
                .setPath(URI);

        ResponseError responseError = handler.handleNoResourceFoundException(
                httpServletRequest,
                new NoResourceFoundException(HttpMethod.GET, "some")
        );

        assertEquals(expected.getPath(), responseError.getPath());
        assertEquals(expected.getError(), responseError.getError());
        assertEquals(expected.getStatus(), responseError.getStatus());
    }

    @Test
    void handleFeignExceptionNotFound() throws JsonProcessingException {
        FeignException.NotFound mockFeignExceptionNotFound = mock(FeignException.NotFound.class);
        when(mockFeignExceptionNotFound.contentUTF8()).thenReturn("some");
        when(objectMapper.readValue(anyString(), eq(ResponseError.class)))
                .thenReturn(new ResponseError().setError("some"));
        when(httpServletRequest.getRequestURI()).thenReturn(URI);
        ResponseError expected = new ResponseError()
                .setStatus(HttpStatus.NOT_FOUND.value())
                .setError("some")
                .setPath(URI);

        ResponseError responseError = handler.handleFeignExceptionNotFound(httpServletRequest, mockFeignExceptionNotFound);

        assertEquals(expected.getPath(), responseError.getPath());
        assertEquals(expected.getError(), responseError.getError());
        assertEquals(expected.getStatus(), responseError.getStatus());
    }

    @Test
    void handleUnknownException() {
        NullPointerException exception = new NullPointerException("some");
        when(httpServletRequest.getRequestURI()).thenReturn(URI);
        ResponseError expected = new ResponseError()
                .setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .setPath(URI);

        ResponseError responseError = handler.handleUnknownException(httpServletRequest, exception);

        assertEquals(expected.getPath(), responseError.getPath());
        assertEquals(expected.getError(), responseError.getError());
        assertEquals(expected.getStatus(), responseError.getStatus());
    }
}