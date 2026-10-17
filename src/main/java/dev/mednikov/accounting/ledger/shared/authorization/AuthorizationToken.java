package dev.mednikov.accounting.ledger.shared.authorization;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class AuthorizationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Object credentials;

    public AuthorizationToken(String token) {
        super(List.of());
        this.credentials = token;
        this.principal = null;
        setAuthenticated(false);
    }

    public AuthorizationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = null;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() { return this.credentials; }

    @Override
    public Object getPrincipal() { return this.principal; }
}
