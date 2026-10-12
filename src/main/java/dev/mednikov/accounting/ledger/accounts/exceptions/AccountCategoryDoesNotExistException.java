package dev.mednikov.accounting.ledger.accounts.exceptions;

import dev.mednikov.accounting.ledger.shared.exceptions.ItemDoesNotExistException;

import java.util.UUID;

public final class AccountCategoryDoesNotExistException extends ItemDoesNotExistException {

    public AccountCategoryDoesNotExistException(UUID id) {
        super("Account category", id);
    }

}
