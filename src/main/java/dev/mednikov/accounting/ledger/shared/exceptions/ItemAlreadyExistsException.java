package dev.mednikov.accounting.ledger.shared.exceptions;

import java.util.UUID;

public class ItemAlreadyExistsException extends RuntimeException{

    public ItemAlreadyExistsException(String entityType, UUID organizationId, String uniqueValue){
        super(String.format("%s with value %s already exists in the organization %s",
                entityType, uniqueValue, organizationId.toString()));
    }
}
