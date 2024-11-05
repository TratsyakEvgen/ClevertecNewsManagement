package ru.clevertec.logging.aspect.integration;

import ru.clevertec.logging.annotation.Log;

public class TestClass {
    @Log
    public long sum(long a, long b) {
        return a + b;
    }
}
