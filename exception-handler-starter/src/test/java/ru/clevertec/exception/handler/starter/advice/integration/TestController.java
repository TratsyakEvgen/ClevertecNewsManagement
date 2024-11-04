package ru.clevertec.exception.handler.starter.advice.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private TestService testService;

    @GetMapping
    public void get() {
        testService.doSome();
    }

    @GetMapping("/{id}")
    public void getWithPathVariable(@PathVariable Long id) {
    }
}
