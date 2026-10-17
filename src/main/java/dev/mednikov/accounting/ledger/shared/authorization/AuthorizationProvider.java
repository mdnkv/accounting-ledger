package dev.mednikov.accounting.ledger.shared.authorization;

import org.jspecify.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationProvider implements AuthenticationProvider {

    private final AuthorizationService authorizationService;

    public AuthorizationProvider(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public @Nullable Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        if (token == null ) {
            throw new RuntimeException("Invalid authorization token.");
        }
        AuthorizedUser authorizedUser = this.authorizationService.getAuthorizedUser(token);
        return new AuthorizationToken(authorizedUser, authorizedUser.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return AuthorizationToken.class.isAssignableFrom(authentication);
    }
}
