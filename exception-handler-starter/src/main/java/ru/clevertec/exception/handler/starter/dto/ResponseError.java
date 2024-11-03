package ru.clevertec.exception.handler.starter.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * DTO ошибки
 */
@Getter
@Setter
@Accessors(chain = true)
public class ResponseError {
    /**
     * Время создания DTO
     */
    private final LocalDateTime timestamp = LocalDateTime.now();
    /**
     * код HTTP статуса
     */
    private int status;
    /**
     * Сообщение об ошибке
     */
    private String error;
    /**
     * URI по которому возникала ошибка
     */
    private String path;

}
