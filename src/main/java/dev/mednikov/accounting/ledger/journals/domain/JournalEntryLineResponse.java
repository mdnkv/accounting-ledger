package dev.mednikov.accounting.ledger.journals.domain;

import dev.mednikov.accounting.ledger.accounts.domain.AccountResponse;

import java.math.BigDecimal;
import java.util.UUID;

public record JournalEntryLineResponse(
        UUID id,
        AccountResponse account,
        BigDecimal debit,
        BigDecimal credit,
        int position
) {
}
