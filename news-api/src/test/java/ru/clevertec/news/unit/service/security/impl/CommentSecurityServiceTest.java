package ru.clevertec.news.unit.service.security.impl;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.service.CacheableCommentService;
import ru.clevertec.news.service.security.SecurityService;
import ru.clevertec.news.service.security.exception.SecurityServiceException;
import ru.clevertec.news.service.security.impl.CommentSecurityService;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentSecurityServiceTest {
    private SecurityService service;
    @Mock
    private CacheableCommentService cacheableCommentService;
    @Mock
    private Authentication authentication;
    @Mock
    private RequestAuthorizationContext context;
    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setUp() {
        service = new CommentSecurityService(cacheableCommentService);
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
    void getDecision_ifRoleSubscriber() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"))).when(authentication).getAuthorities();
        Map<String, String> variables = Map.of("newsId", "1", "commentId", "1");
        Comment comment = new Comment().setUsername("user");
        when(context.getVariables()).thenReturn(variables);
        when(cacheableCommentService.find(1, 1)).thenReturn(comment);
        when(authentication.getName()).thenReturn("user");

        assertTrue(service.getDecision(() -> authentication, context).isGranted());
    }

    @Test
    void getDecision_ifCommentNotFound() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"))).when(authentication).getAuthorities();
        Map<String, String> variables = Map.of("newsId", "2", "commentId", "1");
        when(context.getVariables()).thenReturn(variables);
        when(cacheableCommentService.find(2, 1)).thenThrow(EntityNotFoundException.class);

        assertThrows(AccessDeniedException.class, () -> service.getDecision(() -> authentication, context));
    }

    @Test
    void getDecision_ifNotNewsIdInPath() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"))).when(authentication).getAuthorities();
        Map<String, String> variables = Map.of("commentId", "1");
        when(context.getVariables()).thenReturn(variables);
        when(context.getRequest()).thenReturn(httpServletRequest);

        assertThrows(AccessDeniedException.class, () -> service.getDecision(() -> authentication, context));
    }

    @Test
    void getDecision_ifNewsIdIsNotLong() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"))).when(authentication).getAuthorities();
        Map<String, String> variables = Map.of("newsId", "ss");
        when(context.getVariables()).thenReturn(variables);

        assertThrows(AccessDeniedException.class, () -> service.getDecision(() -> authentication, context));
    }

    @Test
    void getDecision_ifNotCommentIdInPath() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"))).when(authentication).getAuthorities();
        Map<String, String> variables = Map.of("newsId", "1");
        when(context.getVariables()).thenReturn(variables);
        when(context.getRequest()).thenReturn(httpServletRequest);

        assertThrows(AccessDeniedException.class, () -> service.getDecision(() -> authentication, context));
    }

    @Test
    void getDecision_ifCommentIdIsNotLong() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"))).when(authentication).getAuthorities();
        Map<String, String> variables = Map.of("newsId", "1", "commentId", "sss");
        when(context.getVariables()).thenReturn(variables);

        assertThrows(AccessDeniedException.class, () -> service.getDecision(() -> authentication, context));
    }

    @Test
    void getDecision_ifUnknownRole() {
        doReturn(List.of(new SimpleGrantedAuthority("ROLE_GUEST"))).when(authentication).getAuthorities();

        assertFalse(service.getDecision(() -> authentication, context).isGranted());
    }
}