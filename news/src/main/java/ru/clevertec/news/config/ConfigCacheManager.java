package ru.clevertec.news.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
public class ConfigCacheManager {
    @NotBlank(message = "Algorithm must not be empty")
    private String algorithm;
    @Min(value = 1, message = "Capacity must be greater then 0")
    private int capacity;
}
