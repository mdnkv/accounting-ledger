package dev.mednikov.accounting.ledger.accounts.services;

import dev.mednikov.accounting.ledger.accounts.domain.AccountResponse;
import dev.mednikov.accounting.ledger.accounts.domain.CreateAccountRequest;
import dev.mednikov.accounting.ledger.accounts.domain.UpdateAccountRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;

import java.util.Optional;
import java.util.UUID;

public interface AccountService {

    AccountResponse createAccount (CreateAccountRequest requestDto);

    AccountResponse updateAccount (UpdateAccountRequest requestDto);

    PaginationResponse<AccountResponse> getAccounts (UUID organizationId, PaginationRequest request);

    Optional<AccountResponse> getAccountById(UUID id);
}
