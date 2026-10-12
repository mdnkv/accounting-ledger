package dev.mednikov.accounting.ledger.accounts.domain;

import dev.mednikov.accounting.ledger.accounts.models.AccountBalanceType;

import java.util.UUID;

public record AccountResponse(
        UUID id,
        UUID organizationId,
        AccountCategoryResponse accountCategory,
        String name,
        String code,
        AccountBalanceType balanceType,
        boolean active
) {
}
