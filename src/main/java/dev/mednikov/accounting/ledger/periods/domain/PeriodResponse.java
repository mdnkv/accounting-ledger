package dev.mednikov.accounting.ledger.periods.domain;

import java.time.LocalDate;
import java.util.UUID;

public record PeriodResponse(
        UUID id,
        UUID organizationId,
        LocalDate periodStart,
        LocalDate periodEnd,
        boolean closed
) {
}
