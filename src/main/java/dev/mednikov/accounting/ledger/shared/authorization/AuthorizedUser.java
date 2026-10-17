package dev.mednikov.accounting.ledger.shared.authorization;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthorizedUser implements UserDetails {

    private List<SimpleGrantedAuthority> authorities;
    private String userId;

    public AuthorizedUser(String userId, String organizationId, List<String> authoritiesList){
        this.authorities = new ArrayList<>();
        for (String authority: authoritiesList){
            this.authorities.add(new SimpleGrantedAuthority(authority));
        }
        this.authorities.add(new SimpleGrantedAuthority(organizationId));
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
        return this.userId;
    }

}
