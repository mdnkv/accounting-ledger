package dev.mednikov.accounting.ledger.accounts.services;

import dev.mednikov.accounting.ledger.accounts.domain.AccountResponse;
import dev.mednikov.accounting.ledger.accounts.domain.CreateAccountRequest;
import dev.mednikov.accounting.ledger.accounts.domain.UpdateAccountRequest;

import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    AccountResponse createAccount (CreateAccountRequest requestDto);

    AccountResponse updateAccount (UpdateAccountRequest requestDto);

    Optional<AccountResponse> getAccountById(UUID id);
}
