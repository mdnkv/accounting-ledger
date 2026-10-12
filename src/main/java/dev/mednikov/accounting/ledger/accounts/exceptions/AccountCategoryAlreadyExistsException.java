package dev.mednikov.accounting.ledger.accounts.exceptions;

import dev.mednikov.accounting.ledger.shared.exceptions.ItemAlreadyExistsException;

import java.util.UUID;

public final class AccountCategoryAlreadyExistsException extends ItemAlreadyExistsException {

    public AccountCategoryAlreadyExistsException(UUID organizationId, String name){
        super("Account category", organizationId, name);
    }

}
