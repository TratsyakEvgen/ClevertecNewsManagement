package ru.clevertec.news.service.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.function.Supplier;

/**
 * Сервис авторизации
 */
public interface SecurityService {
    /**
     * Предоставляет решение об авторизации на основании данных аутентификации и контекста запроса
     *
     * @param authenticationSupplier поставщик данных аутентификации
     * @param context                контекст запроса
     * @return решение об авторизации
     */
    AuthorizationDecision getDecision(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context);
}

