package ru.clevertec.news.unit.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.news.dto.request.AuthenticationData;
import ru.clevertec.news.dto.response.ResponseToken;
import ru.clevertec.news.dto.response.ResponseUser;
import ru.clevertec.news.service.TokenService;
import ru.clevertec.news.service.UserClient;
import ru.clevertec.news.service.impl.DefaultTokenService;
import ru.clevertec.news.service.security.SecretKeyGenerator;
import ru.clevertec.news.util.JwtUtil;

import javax.crypto.SecretKey;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTokenServiceTest {
    private TokenService tokenService;
    @Mock
    private SecretKeyGenerator secretKeyGenerator;
    @Mock
    private UserClient userClient;

    @BeforeEach
    void setUp() {
        tokenService = new DefaultTokenService(secretKeyGenerator, userClient, 600000);
    }

    @Test
    void createToken() {
        AuthenticationData data = new AuthenticationData("user", "password");
        ResponseUser user = new ResponseUser("ADMIN");
        SecretKey secretKey = Jwts.SIG.HS256.key().build();
        when(secretKeyGenerator.generate()).thenReturn(secretKey);
        when(userClient.getUserInfo("user", "password")).thenReturn(user);

        ResponseToken responseToken = tokenService.createToken(data);

        Claims climes = JwtUtil.getClimes(secretKey, responseToken);
        Assertions.assertEquals("user", climes.get("sub"));
        Assertions.assertEquals("ADMIN", climes.get("scope"));
    }
}