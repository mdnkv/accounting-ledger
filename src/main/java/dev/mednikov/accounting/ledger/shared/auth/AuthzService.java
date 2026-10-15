package dev.mednikov.accounting.ledger.shared.auth;

public interface AuthzService {

    AuthorizedUser getAuthorizedUser (String token);

}
