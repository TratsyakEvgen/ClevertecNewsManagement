package ru.clevertec.news.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import ru.clevertec.news.service.security.SecretKeyGenerator;
import ru.clevertec.news.service.security.SecurityService;

import javax.crypto.SecretKey;

/**
 * Конфигурация безопасности
 */
@Configuration
public class SecurityConfiguration {

    /**
     * Конфигурация фильтров Spring Security
     *
     * @param decoder                jtw декодер
     * @param newsSecurityService    сервис авторизации новостей
     * @param commentSecurityService сервис авторизации новостей
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           JwtDecoder decoder,
                                           SecurityService newsSecurityService,
                                           SecurityService commentSecurityService

    ) throws Exception {
        return http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/tokens").permitAll()
                        .requestMatchers(HttpMethod.GET, "/news/**").permitAll()
                        .requestMatchers("/news/{newsId}").access(newsSecurityService::getDecision)
                        .requestMatchers("/news/{newsId}/comments/{commentId}").access(commentSecurityService::getDecision)
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(decoder)))
                .build();
    }

    /**
     * Конвертер, добавляющий префикс ROLE_ к значениям переданным в scope jwt токена
     *
     * @return конвертер jwt токенов
     */
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }


    /**
     * Конфигурация декодера JWT токенов доступа
     *
     * @param secretKeyGenerator генератор секретных ключей
     * @return jwt декодер
     */
    @Bean
    public JwtDecoder jwtDecoder(SecretKeyGenerator secretKeyGenerator) {
        SecretKey secretKey = secretKeyGenerator.generate();
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
    }
}
