package ru.clevertec.news.service.security;

import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public abstract class AbstractSecurityService implements SecurityService{
    protected Map<String, BiPredicate<Authentication, RequestAuthorizationContext>> roleDecisionMap = new HashMap<>();
    @Override
    public AuthorizationDecision getDecision(Supplier<Authentication> authenticationSupplier, RequestAuthorizationContext context) {
        Authentication authentication = authenticationSupplier.get();
        Set<String> strings = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        boolean granted = strings.stream()
                .filter(s -> roleDecisionMap .containsKey(s))
                .anyMatch(s -> roleDecisionMap .get(s).test(authentication, context));
        return new AuthorizationDecision(granted);
    }
    protected abstract boolean isGranted(Authentication authentication, RequestAuthorizationContext context);
}
