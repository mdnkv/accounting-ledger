package dev.mednikov.accounting.ledger.shared.authorization;

import cn.hutool.jwt.JWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorizationServiceImpl implements AuthorizationService{

    private final byte[] key;

    public AuthorizationServiceImpl(@Value("${app.signing-key}") String signingKey){
        this.key = signingKey.getBytes();
    }


    @Override
    public AuthorizedUser getAuthorizedUser(String token) {
        List<String> authoritiesList = (List<String>) JWT.of(token).setKey(this.key).getPayload().getClaim("authorities");
        String organizationId = (String) JWT.of(token).getPayload().getClaim("organizationId");
        List<String> authorities = new ArrayList<>(authoritiesList);
        authorities.add(organizationId);
        return new AuthorizedUser(authorities);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
