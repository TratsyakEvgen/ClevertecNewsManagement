package ru.clevertec.exception.handler.starter.exception;

/**
 * Исключение генерируемое в случае если искомая сущность уже существует
 */
public class EntityAlreadyExistsException extends RuntimeException {
    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
