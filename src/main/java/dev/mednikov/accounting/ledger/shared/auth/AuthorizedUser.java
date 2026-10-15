package dev.mednikov.accounting.ledger.shared.auth;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class AuthorizedUser implements UserDetails {

    private final List<SimpleGrantedAuthority> authorities;

    public AuthorizedUser(List<String> authoritiesList){
        this.authorities = authoritiesList.stream().map(SimpleGrantedAuthority::new).toList();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return "";
    }

}
