package ru.clevertec.news.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import ru.clevertec.news.dto.response.ResponseToken;

import javax.crypto.SecretKey;

public class JwtUtil {
    public static String createToken(SecretKey secretKey, String username, String role) {
        return Jwts.builder()
                .signWith(secretKey)
                .subject(username)
                .claim("scope", role)
                .compact();
    }

    public static Claims getClimes(SecretKey secretKey, ResponseToken responseToken) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(responseToken.getToken())
                .getPayload();
    }
}
