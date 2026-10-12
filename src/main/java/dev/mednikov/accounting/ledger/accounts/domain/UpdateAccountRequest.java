package dev.mednikov.accounting.ledger.accounts.domain;

import dev.mednikov.accounting.ledger.accounts.models.AccountBalanceType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record UpdateAccountRequest(
        @NotNull(message = "Account id cannot be null") UUID id,
        @NotNull(message = "Organization id cannot be null") UUID organizationId,
        @NotNull(message = "Account category id cannot be null") UUID accountCategoryId,

        @NotNull(message = "Account name cannot be null")
        @NotEmpty(message = "Account name cannot be empty")
        @Length(max = 255, message = "Account name cannot exceed 255 characters")
        String name,

        @NotNull(message = "Account code cannot be null")
        @NotEmpty(message = "Account code cannot be empty")
        @Length(max = 25, message = "Account code cannot exceed 25 characters")
        String code,

        @NotNull(message = "Account balance type cannot be null")
        AccountBalanceType balanceType,

        @NotNull(message = "This field cannot be empty")
        boolean active
) {
}
