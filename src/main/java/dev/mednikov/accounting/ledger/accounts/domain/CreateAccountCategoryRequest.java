package dev.mednikov.accounting.ledger.accounts.domain;

import dev.mednikov.accounting.ledger.accounts.models.AccountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

public record CreateAccountCategoryRequest(
        @NotNull(message = "Organization id cannot be null") UUID organizationId,
        @NotNull(message = "Account category name cannot be null")
        @NotBlank(message = "Account category name cannot be empty")
        @Length(max = 255, message = "Max length for this field is 255 characters")
        String name,

        @NotNull(message = "Account type cannot be null")
        AccountType accountType
) {
}
