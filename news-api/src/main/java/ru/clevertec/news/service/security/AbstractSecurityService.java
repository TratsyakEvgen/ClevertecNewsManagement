package ru.clevertec.news.service.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import ru.clevertec.news.service.security.exception.SecurityServiceException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

/**
 * Абстрактный сервис авторизации
 */
public abstract class AbstractSecurityService implements SecurityService {
    protected Map<String, BiPredicate<Authentication, RequestAuthorizationContext>> roleDecisionMap = new HashMap<>();

    /**
     * Предоставляет решение об авторизации на основании данных аутентификации и контекста запроса.
     * Проверяет роли пользователя на наличие в roleDecisionMap.
     * В случае совпадения роли принимается решение об авторизации на основании переданных параметров
     *
     * @param authenticationSupplier поставщик данных аутентификации
     * @param context                контекст запроса
     * @return решение об авторизации
     * @throws SecurityServiceException если параметр null
     */
    @Override
    public AuthorizationDecision getDecision(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        if (authenticationSupplier == null || context == null) {
            throw new SecurityServiceException("Parameters must not be null");
        }

        Authentication authentication = authenticationSupplier.get();
        Set<String> strings = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        boolean granted = strings.stream()
                .filter(s -> roleDecisionMap.containsKey(s))
                .anyMatch(s -> roleDecisionMap.get(s).test(authentication, context));

        return new AuthorizationDecision(granted);
    }

    /**
     * Абстрактный метод проверки авторизации
     *
     * @param authentication данные аутентификации
     * @param context        контекст запроса
     * @return решение об авторизации
     */
    protected abstract boolean isAuthorized(Authentication authentication, RequestAuthorizationContext context);
}
