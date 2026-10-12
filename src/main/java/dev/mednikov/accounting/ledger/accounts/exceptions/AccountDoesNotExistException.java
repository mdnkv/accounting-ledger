package dev.mednikov.accounting.ledger.accounts.exceptions;

import dev.mednikov.accounting.ledger.shared.exceptions.ItemDoesNotExistException;

import java.util.UUID;

public final class AccountDoesNotExistException extends ItemDoesNotExistException {

    public AccountDoesNotExistException(UUID id){
        super("Account", id);
    }

}