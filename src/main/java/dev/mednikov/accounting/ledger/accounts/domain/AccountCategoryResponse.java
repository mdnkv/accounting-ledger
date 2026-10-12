package dev.mednikov.accounting.ledger.accounts.domain;

import dev.mednikov.accounting.ledger.accounts.models.AccountType;

import java.util.UUID;

public record AccountCategoryResponse (
        UUID id,
        UUID organizationId,
        String name,
        AccountType accountType,
        boolean active
) {}
