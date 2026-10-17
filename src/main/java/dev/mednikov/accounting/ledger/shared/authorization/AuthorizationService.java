package dev.mednikov.accounting.ledger.shared.authorization;

public interface AuthorizationService {

    AuthorizedUser getAuthorizedUser (String token);

}
