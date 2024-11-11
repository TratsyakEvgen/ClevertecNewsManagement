package ru.clevertec.exception.handler.starter.advice.integration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Profile("test")
public class TestController {
    private final TestService testService;

    @GetMapping
    public void get() {
        testService.doSome();
    }

    @GetMapping("/{id}")
    public void getWithPathVariable(@PathVariable Long id) {
    }

    @PostMapping
    public void postWithBody(@RequestBody String body) {
    }
}
