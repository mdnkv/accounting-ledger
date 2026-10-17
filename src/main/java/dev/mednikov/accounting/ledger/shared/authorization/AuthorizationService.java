package dev.mednikov.accounting.ledger.shared.authorization;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthorizationService extends UserDetailsService {

    AuthorizedUser getAuthorizedUser (String token);

}
