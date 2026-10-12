package dev.mednikov.accounting.ledger.accounts.mappers;

import dev.mednikov.accounting.ledger.accounts.domain.AccountResponse;
import dev.mednikov.accounting.ledger.accounts.models.Account;

import java.util.function.Function;

public interface AccountResponseMapper extends Function<Account, AccountResponse> {
}
