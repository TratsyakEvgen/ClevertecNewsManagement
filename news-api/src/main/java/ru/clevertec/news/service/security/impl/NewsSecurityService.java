package ru.clevertec.news.service.security.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;
import ru.clevertec.news.service.CacheableNewsService;
import ru.clevertec.news.service.security.AbstractSecurityService;
import ru.clevertec.news.service.security.exception.SecurityServiceException;

import java.util.Optional;

/**
 * Сервис авторизации для новостей
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class NewsSecurityService extends AbstractSecurityService {
    private final CacheableNewsService newsService;

    {
        roleDecisionMap.put("ROLE_ADMIN", (authentication, context) -> true);
        roleDecisionMap.put("ROLE_JOURNALIST", this::isAuthorized);
    }

    /**
     * Сопоставляет имя пользователя и автора новости
     *
     * @param authentication данные аутентификации
     * @param context        контекст запроса
     * @return решение об авторизации
     * @throws AccessDeniedException если в пути запроса отсутствует newsId,
     *                               newsId не является Long типом
     *                               новость с newsId не существует
     */
    protected boolean isAuthorized(Authentication authentication, RequestAuthorizationContext context) {
        try {
            return Optional.ofNullable(context.getVariables().get("newsId"))
                    .map(stringId -> newsService.find(Long.parseLong(stringId)))
                    .map(news -> news.getUsername().equals(authentication.getName()))
                    .orElseThrow(() ->
                            new SecurityServiceException("Not found news id in " + context.getRequest().getRequestURI())
                    );
        } catch (NumberFormatException | EntityNotFoundException | SecurityServiceException e) {
            log.warn("Security exception", e);
            throw new AccessDeniedException(e.getMessage());
        }
    }

}
