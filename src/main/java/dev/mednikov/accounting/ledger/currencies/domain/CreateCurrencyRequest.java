package dev.mednikov.accounting.ledger.currencies.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record CreateCurrencyRequest(
        @NotNull(message = "Organization id cannot be null") UUID organizationId,

        @NotNull(message = "Currency name cannot be null")
        @NotEmpty(message = "Currency name cannot be empty")
        @Length(max = 255, message = "This field cannot exceed 255 characters")
        String name,

        @NotNull(message = "Currency code cannot be null")
        @NotEmpty(message = "Currency code cannot be empty")
        @Length(max = 3, min = 3, message = "This field cannot exceed 3 characters")
        String code
) {
}
