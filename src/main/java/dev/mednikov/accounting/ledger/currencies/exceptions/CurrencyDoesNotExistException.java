package dev.mednikov.accounting.ledger.currencies.exceptions;

import dev.mednikov.accounting.ledger.shared.exceptions.ItemDoesNotExistException;

import java.util.UUID;

public final class CurrencyDoesNotExistException extends ItemDoesNotExistException {

    public CurrencyDoesNotExistException(UUID id){
        super("Currency", id);
    }

}
