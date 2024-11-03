package ru.clevertec.cache.exception;

/**
 * Исключение выбрасываемое, если передан несуществующий алгоритм кэша
 */
public class IncorrectAlgorithmException extends RuntimeException {
    public IncorrectAlgorithmException(String message) {
        super(message);
    }
}
