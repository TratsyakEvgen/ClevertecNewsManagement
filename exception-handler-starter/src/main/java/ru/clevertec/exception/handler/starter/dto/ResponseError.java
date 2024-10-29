package ru.clevertec.exception.handler.starter.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
public class ResponseError {
    private final LocalDateTime timestamp = LocalDateTime.now();
    private int status;
    private String error;
    private String path;

}
