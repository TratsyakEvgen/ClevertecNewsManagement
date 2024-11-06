package ru.clevertec.news.unit.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.news.controller.TokenController;
import ru.clevertec.news.dto.request.AuthenticationData;
import ru.clevertec.news.dto.response.ResponseToken;
import ru.clevertec.news.service.TokenService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenControllerTest {
    private TokenController tokenController;
    @Mock
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        tokenController = new TokenController(tokenService);
    }

    @Test
    void createToken() {
        AuthenticationData data = new AuthenticationData("user", "password");
        ResponseToken responseToken = new ResponseToken("token");
        when(tokenService.createToken(any())).thenReturn(responseToken);

        ResponseToken actual = tokenController.createToken(data);

        assertEquals(responseToken, actual);
    }
}