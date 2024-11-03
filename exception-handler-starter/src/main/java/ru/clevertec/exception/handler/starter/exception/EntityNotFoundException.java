package ru.clevertec.exception.handler.starter.exception;

/**
 * Исключение генерируемое в случае если искомая сущность не найдена
 */
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message) {
        super(message);
    }
}
