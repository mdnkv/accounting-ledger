package dev.mednikov.accounting.ledger.currencies.domain;

import java.util.UUID;

public record CurrencyResponse(
        UUID id,
        UUID organizationId,
        String name,
        String code,
        boolean primary,
        boolean active
) {
}
