package dev.mednikov.accounting.ledger.shared.auth;

import cn.hutool.jwt.JWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class AuthzServiceImpl implements AuthzService, UserDetailsService {

    private final byte[] key;

    public AuthzServiceImpl(@Value("${app.signing-key}") String signingKey){
        this.key = signingKey.getBytes();
    }

    @Override
    public AuthorizedUser getAuthorizedUser(String token) {
        List<String> authorities = (List<String>) JWT.of(token).setKey(this.key).getPayload().getClaim("authorities");
        return new AuthorizedUser(authorities);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
