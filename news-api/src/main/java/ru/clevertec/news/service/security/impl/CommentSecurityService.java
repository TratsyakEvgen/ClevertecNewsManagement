package ru.clevertec.news.service.security.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import ru.clevertec.news.entity.Comment;
import ru.clevertec.news.service.CacheableCommentService;
import ru.clevertec.news.service.security.AbstractSecurityService;
import ru.clevertec.news.service.security.exception.SecurityServiceException;

import java.util.Map;
import java.util.Optional;

/**
 * Сервис авторизации для комментариев
 */
@Component
@RequiredArgsConstructor
public class CommentSecurityService extends AbstractSecurityService {
    private final CacheableCommentService commentService;

    {
        roleDecisionMap.put("ROLE_ADMIN", (authentication, context) -> true);
        roleDecisionMap.put("ROLE_SUBSCRIBER", this::isAuthorized);
    }

    /**
     * Сопоставляет имя пользователя и автора комментария
     *
     * @param authentication данные аутентификации
     * @param context        контекст запроса
     * @return решение об авторизации
     * @throws SecurityServiceException если в пути запроса отсутствует newsId или commentId
     */
    @Override
    protected boolean isAuthorized(Authentication authentication, RequestAuthorizationContext context) {
        Map<String, String> variables = context.getVariables();
        long newsId = Optional.ofNullable(variables.get("newsId"))
                .map(Long::parseLong)
                .orElseThrow(() ->
                        new SecurityServiceException("Not found news id in " + context.getRequest().getRequestURI())
                );
        long commentId = Optional.ofNullable(variables.get("commentId"))
                .map(Long::parseLong)
                .orElseThrow(() ->
                        new SecurityServiceException("Not found comment id in " + context.getRequest().getRequestURI()));
        Comment comment = commentService.find(newsId, commentId);
        return comment.getUsername().equals(authentication.getName());
    }
}
