package dev.mednikov.accounting.ledger.journals.exceptions;

public final class NotBalancedEntryException extends RuntimeException{

    public NotBalancedEntryException(){
        super("Debit is not equal to credit");
    }

}
