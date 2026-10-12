package dev.mednikov.accounting.ledger.journals.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateJournalEntryLineRequest(
        @NotNull(message = "Account id cannot be null") UUID accountId,

        @NotNull(message = "Debit cannot be empty")
        @PositiveOrZero(message = "Debit cannot be negative")
        BigDecimal debit,

        @NotNull(message = "Credit cannot be empty")
        @PositiveOrZero(message = "Credit cannot be negative")
        BigDecimal credit,

        @NotNull(message = "Position cannot be empty")
        @Positive(message = "Position cannot be negative")
        int position
) {
}
