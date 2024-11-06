package ru.clevertec.news.unit.service.security.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import ru.clevertec.news.entity.News;
import ru.clevertec.news.service.CacheableNewsService;
import ru.clevertec.news.service.security.SecurityService;
import ru.clevertec.news.service.security.exception.SecurityServiceException;
import ru.clevertec.news.service.security.impl.NewsSecurityService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsSecurityServiceTest {

    private SecurityService service;
    @Mock
    private CacheableNewsService cacheableNewsService;
    @Mock
    private Authentication authentication;
    @Mock
    private RequestAuthorizationContext context;
    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        service = new NewsSecurityService(cacheableNewsService);
    }

    @Test
    void getDecision_ifParamsIsNull() {
        assertThrows(SecurityServiceException.class, () -> service.getDecision(null, null));

    }

    @Test
    void getDecision_ifRoleAdmin() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))).when(authentication).getAuthorities();

        assertTrue(service.getDecision(() -> authentication, context).isGranted());
    }

    @Test
    void getDecision_ifRoleJournalist() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_JOURNALIST"))).when(authentication).getAuthorities();
        Map<String, String> variables = Map.of("newsId", "1");
        News news = new News().setUsername("user");
        when(context.getVariables()).thenReturn(variables);
        when(cacheableNewsService.find(1)).thenReturn(news);
        when(authentication.getName()).thenReturn("user");

        assertTrue(service.getDecision(() -> authentication, context).isGranted());
    }

    @Test
    void getDecision_ifNotNewsIdInPath() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_JOURNALIST"))).when(authentication).getAuthorities();
        Map<String, String> variables = Map.of();
        when(context.getVariables()).thenReturn(variables);
        when(context.getRequest()).thenReturn(httpServletRequest);

        assertThrows(SecurityServiceException.class, () -> service.getDecision(() -> authentication, context));
    }

    @Test
    void getDecision_ifUnknownRole() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_GUEST"))).when(authentication).getAuthorities();

        assertFalse(service.getDecision(() -> authentication, context).isGranted());
    }
}