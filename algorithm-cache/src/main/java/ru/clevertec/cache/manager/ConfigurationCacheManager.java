package ru.clevertec.cache.manager;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

/**
 * Конфигурация кэш менеджера
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Validated
public class ConfigurationCacheManager {
    /**
     * Алгоритм кэширования
     */
    @NotBlank(message = "Algorithm must not be empty")
    private String algorithm;
    /**
     * Максимальный размер кэша
     */
    @Min(value = 1, message = "Capacity must be greater then 0")
    private int capacity;
}
