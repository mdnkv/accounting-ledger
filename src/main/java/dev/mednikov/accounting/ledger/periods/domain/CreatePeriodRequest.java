package dev.mednikov.accounting.ledger.periods.domain;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record CreatePeriodRequest(
        @NotNull(message = "Organization id cannot be null") UUID organizationId,
        @NotNull(message = "Period start date cannot be null") LocalDate periodStart,
        @NotNull(message = "Period end date cannot be null") LocalDate periodEnd
) {
}
