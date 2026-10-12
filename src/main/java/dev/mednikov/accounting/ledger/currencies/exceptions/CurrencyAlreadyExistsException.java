package dev.mednikov.accounting.ledger.currencies.exceptions;

import dev.mednikov.accounting.ledger.shared.exceptions.ItemAlreadyExistsException;

import java.util.UUID;

public final class CurrencyAlreadyExistsException extends ItemAlreadyExistsException {

    public CurrencyAlreadyExistsException(UUID organizationId, String currencyCode){
        super("Currency", organizationId, currencyCode);
    }

}
