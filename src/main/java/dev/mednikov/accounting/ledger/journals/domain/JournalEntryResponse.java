package dev.mednikov.accounting.ledger.journals.domain;

import dev.mednikov.accounting.ledger.currencies.domain.CurrencyResponse;
import dev.mednikov.accounting.ledger.journals.models.JournalEntryType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record JournalEntryResponse(
        UUID id,
        UUID organizationId,
        JournalEntryType journalEntryType,
        CurrencyResponse currency,
        String description,
        LocalDate date,
        BigDecimal totalDebit,
        BigDecimal totalCredit
) {
}
