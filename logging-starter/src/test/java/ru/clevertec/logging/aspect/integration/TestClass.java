package ru.clevertec.logging.aspect.integration;

import org.springframework.boot.test.context.TestComponent;
import ru.clevertec.logging.annotation.Log;

@TestComponent
public class TestClass {
    @Log
    public long sum(long a, long b) {
        return a + b;
    }
}
