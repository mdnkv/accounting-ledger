package dev.mednikov.accounting.ledger.shared.exceptions;

import java.util.UUID;

public class ItemDoesNotExistException extends RuntimeException{

    public ItemDoesNotExistException (String entityType, UUID id){
        super(String.format("%s with id %s does not exist", entityType, id.toString()));
    }

}
