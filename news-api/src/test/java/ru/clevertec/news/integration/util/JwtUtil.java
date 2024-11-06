package ru.clevertec.news.integration.util;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class JwtUtil {
    public static String createToken(SecretKey secretKey, String username, String role) {
        return Jwts.builder()
                .signWith(secretKey)
                .subject(username)
                .claim("scope", role)
                .compact();
    }
}
