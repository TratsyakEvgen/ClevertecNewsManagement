package ru.clevertec.news.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.clevertec.news.dto.request.AuthenticationData;
import ru.clevertec.news.dto.response.ResponseToken;
import ru.clevertec.news.service.TokenService;

import javax.crypto.SecretKey;
import java.util.Arrays;

@Service
@Validated
public class DefaultTokenService implements TokenService {
    @Override
    public ResponseToken createToken(AuthenticationData authenticationData) {
        String jwtSecret = "4261656C64756E67";

        byte[] keyStrict = Arrays.copyOf(jwtSecret.getBytes(), 256);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyStrict);
        System.out.println(secretKey.getAlgorithm());

        String token = Jwts.builder()
//                .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(secretKey)
                .subject("user")
                .claim("scope", "ADMIN")
                .compact();
        return new ResponseToken(token);
    }
}
