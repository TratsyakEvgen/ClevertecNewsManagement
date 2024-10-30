package ru.clevertec.user;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Date;

@RestController
public class UserController {
    @GetMapping
    public String get() {
        String jwtSecret = "4261656C64756E67";

        byte[] keyStrict = Arrays.copyOf(jwtSecret.getBytes(), 256);
      //  byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyStrict);
        System.out.println(secretKey.getAlgorithm());

//        String keyString = "стоfdvfdfgdfgdfffgfgdfgdfgf";
//
//        SecretKey secretKeySpec = new SecretKeySpec(keyStrict, "HS256");

        return Jwts.builder()
//                .expiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(secretKey)
                .subject("user")
                .compact();

    }
}
