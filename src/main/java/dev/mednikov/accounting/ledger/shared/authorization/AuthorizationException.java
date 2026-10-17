package dev.mednikov.accounting.ledger.shared.authorization;

public final class AuthorizationException extends RuntimeException{

    public AuthorizationException(String message){
        super(message);
    }

}
