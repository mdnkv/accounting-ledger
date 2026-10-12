package dev.mednikov.accounting.ledger.journals.domain;

import dev.mednikov.accounting.ledger.journals.models.JournalEntryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateJournalEntryRequest(
        @NotNull(message = "Organization id cannot be null") UUID organizationId,
        @NotNull(message = "Currency id cannot be null") UUID currencyId,
        @NotNull(message = "Journal entry type cannot be null") JournalEntryType journalEntryType,

        @NotNull(message = "Description cannot be null")
        @NotBlank(message = "Description cannot be blank")
        String description,

        @NotNull(message = "Date cannot be blank")
        LocalDate date,

        @NotEmpty(message = "Journal entries list cannot be empty")
        List<CreateJournalEntryLineRequest> journalEntryLines
) {
}
