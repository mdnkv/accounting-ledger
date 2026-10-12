package dev.mednikov.accounting.ledger.accounts.exceptions;

import dev.mednikov.accounting.ledger.shared.exceptions.ItemAlreadyExistsException;

import java.util.UUID;

public final class AccountAlreadyExistsException extends ItemAlreadyExistsException {

    public AccountAlreadyExistsException(UUID organizationId, String code){
        super("Account", organizationId, code);
    }
}
