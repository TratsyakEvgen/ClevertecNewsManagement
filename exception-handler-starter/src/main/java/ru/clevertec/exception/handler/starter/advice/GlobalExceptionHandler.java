package ru.clevertec.exception.handler.starter.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.clevertec.exception.handler.starter.dto.ResponseError;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseError processConstraintViolationException(HttpServletRequest request, ConstraintViolationException e) {
        String messages = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        return new ResponseError().setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .setError(messages)
                .setPath(request.getRequestURI());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseError processNotFoundException(HttpServletRequest request, EntityNotFoundException e) {
        return new ResponseError().setStatus(HttpStatus.NOT_FOUND.value())
                .setError(e.getMessage())
                .setPath(request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseError processUnknownException(HttpServletRequest request) {
        return new ResponseError().setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .setPath(request.getRequestURI());
    }

}
