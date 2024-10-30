package ru.clevertec.news.service.security.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;
import ru.clevertec.news.service.CacheableNewsService;
import ru.clevertec.news.service.security.AbstractSecurityService;
import ru.clevertec.news.service.security.exception.SecurityServiceException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NewsSecurityService extends AbstractSecurityService {
    private final CacheableNewsService newsService;

    {
        roleDecisionMap.put("ROLE_ADMIN", (authentication, context) -> true);
        roleDecisionMap.put("ROLE_JOURNALIST", this::isGranted);
    }

    protected boolean isGranted(Authentication authentication, RequestAuthorizationContext context) {
        return Optional.ofNullable(context.getVariables().get("newsId"))
                .map(stringId -> newsService.find(Long.parseLong(stringId)))
                .map(news -> news.getUsername().equals(authentication.getName()))
                .orElseThrow(() ->
                        new SecurityServiceException("Not found news id in " + context.getRequest().getRequestURI())
                );
    }

}
