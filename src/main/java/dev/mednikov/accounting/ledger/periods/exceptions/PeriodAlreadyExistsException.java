package dev.mednikov.accounting.ledger.periods.exceptions;

import dev.mednikov.accounting.ledger.shared.exceptions.ItemAlreadyExistsException;

import java.time.LocalDate;
import java.util.UUID;

public final class PeriodAlreadyExistsException extends ItemAlreadyExistsException {

    public PeriodAlreadyExistsException(UUID organizationId, LocalDate periodStart, LocalDate periodEnd) {
        super("Period", organizationId, periodStart.toString() + " - " + periodEnd.toString());
    }
}
