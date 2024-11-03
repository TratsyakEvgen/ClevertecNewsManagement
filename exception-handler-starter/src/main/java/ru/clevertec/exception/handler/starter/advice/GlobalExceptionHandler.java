package ru.clevertec.exception.handler.starter.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.clevertec.exception.handler.starter.dto.ResponseError;
import ru.clevertec.exception.handler.starter.exception.EntityAlreadyExistsException;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;

import java.util.stream.Collectors;

/**
 * Обработчик исключений
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final ObjectMapper objectMapper;

    /**
     * Обработчик {@link ConstraintViolationException}
     *
     * @return DTO ошибки
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        log.warn("Not valid data", e);
        String messages = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        return new ResponseError().setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .setError(messages)
                .setPath(request.getRequestURI());
    }

    /**
     * Обработчик {@link EntityNotFoundException}
     *
     * @return DTO ошибки
     */
    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleNotFoundException(HttpServletRequest request, EntityNotFoundException e) {
        log.warn("Not found entity", e);
        return new ResponseError().setStatus(HttpStatus.NOT_FOUND.value())
                .setError(e.getMessage())
                .setPath(request.getRequestURI());
    }

    /**
     * Обработчик {@link EntityAlreadyExistsException}
     *
     * @return DTO ошибки
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleEntityAlreadyExistsException(HttpServletRequest request, EntityAlreadyExistsException e) {
        log.warn("Entity already exists", e);
        return new ResponseError().setStatus(HttpStatus.BAD_REQUEST.value())
                .setError(e.getMessage())
                .setPath(request.getRequestURI());
    }

    /**
     * Обработчик {@link MethodArgumentTypeMismatchException}
     *
     * @return DTO ошибки
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseError handleMethodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
        log.warn("Incorrect path", e);
        return new ResponseError().setStatus(HttpStatus.BAD_REQUEST.value())
                .setError(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .setPath(request.getRequestURI());
    }

    /**
     * Обработчик {@link FeignException}
     *
     * @return DTO ошибки
     */
    @ExceptionHandler(FeignException.NotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError handleFeignException(HttpServletRequest request, FeignException.NotFound e) throws JsonProcessingException {
        log.warn("Incorrect path", e);
        ResponseError feignResponseError = objectMapper.readValue(e.contentUTF8(), ResponseError.class);
        return new ResponseError().setStatus(HttpStatus.NOT_FOUND.value())
                .setError(feignResponseError.getError())
                .setPath(request.getRequestURI());
    }

    /**
     * Обработчик всех незарегистрированных исключений, расширяющих {@link Exception}
     *
     * @return DTO ошибки
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError handleUnknownException(HttpServletRequest request, Exception e) {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), e);
        return new ResponseError().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .setPath(request.getRequestURI());
    }

}
