package ru.clevertec.news.service.security.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import ru.clevertec.exception.handler.starter.exception.EntityNotFoundException;
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
@Slf4j
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
     *                                  если комментарий не найден
     *                                  если newsId или commentId не является типом long
     */
    @Override
    protected boolean isAuthorized(Authentication authentication, RequestAuthorizationContext context) {
        try {
            long newsId = getNewsId(context);
            long commentId = getCommentId(context);
            Comment comment = commentService.find(newsId, commentId);
            return comment.getUsername().equals(authentication.getName());
        } catch (NumberFormatException | EntityNotFoundException | SecurityServiceException e) {
            log.warn("Security exception", e);
            throw new AccessDeniedException(e.getMessage());
        }
    }

    /**
     * Предоставляет newsId из запроса
     *
     * @param context контекст запроса
     * @return newsId
     * @throws SecurityServiceException если отсутствует newsId
     * @throws NumberFormatException    если newsId не является типом long
     */
    private long getNewsId(RequestAuthorizationContext context) {
        Map<String, String> variables = context.getVariables();
        return Optional.ofNullable(variables.get("newsId"))
                .map(Long::parseLong)
                .orElseThrow(() ->
                        new SecurityServiceException("Not found news id in " + context.getRequest().getRequestURI())
                );
    }

    /**
     * Предоставляет getCommentId из запроса
     *
     * @param context контекст запроса
     * @return newsId
     * @throws SecurityServiceException если отсутствует getCommentId
     * @throws NumberFormatException    если newsId не является типом getCommentId
     */
    private long getCommentId(RequestAuthorizationContext context) {
        Map<String, String> variables = context.getVariables();
        return Optional.ofNullable(variables.get("commentId"))
                .map(Long::parseLong)
                .orElseThrow(() ->
                        new SecurityServiceException("Not found comment id in " + context.getRequest().getRequestURI()));
    }
}
