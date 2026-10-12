package dev.mednikov.accounting.ledger.accounts.mappers;

import dev.mednikov.accounting.ledger.accounts.domain.AccountCategoryResponse;
import dev.mednikov.accounting.ledger.accounts.models.AccountCategory;

import java.util.function.Function;

public interface AccountCategoryResponseMapper extends Function<AccountCategory, AccountCategoryResponse> {
}
