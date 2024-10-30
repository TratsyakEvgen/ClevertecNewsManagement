package ru.clevertec.news.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.news.dto.request.AuthenticationData;
import ru.clevertec.news.dto.response.ResponseToken;
import ru.clevertec.news.service.TokenService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tokens")
public class TokenController {
    private final TokenService tokenService;

    @PostMapping
    public ResponseToken createToken(@RequestBody AuthenticationData authenticationData) {
        return tokenService.createToken(authenticationData);
    }
}
