
package ru.clevertec.news.configuration;

import io.jsonwebtoken.security.Keys;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
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
import ru.clevertec.cache.manager.AlgorithmCacheManager;
import ru.clevertec.cache.manager.ConfigurationCacheManager;
import ru.clevertec.news.service.security.SecurityService;

import javax.crypto.SecretKey;
import java.util.Arrays;


@org.springframework.context.annotation.Configuration
@EnableCaching
public class Configuration {
    @Bean
    @ConfigurationProperties(prefix = "cache")
    public ConfigurationCacheManager configCacheManager() {
        return new ConfigurationCacheManager();
    }

    @Bean
    public CacheManager cacheManager(ConfigurationCacheManager config) {
        return new AlgorithmCacheManager(config);
    }

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
                        .requestMatchers("/tokens")
                        .permitAll()
                        .requestMatchers(HttpMethod.GET, "/news/**")
                        .permitAll()
                        .requestMatchers("/news/{newsId}")
                        .access(newsSecurityService::getDecision)
                        .requestMatchers("/news/{newsId}/comments/{commentId}")
                        .access(commentSecurityService::getDecision)
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(decoder)))
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        String jwtSecret = "4261656C64756E67";
        byte[] keyStrict = Arrays.copyOf(jwtSecret.getBytes(), 256);
        SecretKey secretKey = Keys.hmacShaKeyFor(keyStrict);
        return NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS512).build();
    }
}
