package dev.mednikov.accounting.ledger.accounts.services;

import dev.mednikov.accounting.ledger.accounts.domain.AccountCategoryResponse;
import dev.mednikov.accounting.ledger.accounts.domain.CreateAccountCategoryRequest;
import dev.mednikov.accounting.ledger.accounts.domain.UpdateAccountCategoryRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;

import java.util.Optional;
import java.util.UUID;

public interface AccountCategoryService {

    AccountCategoryResponse createAccountCategory (CreateAccountCategoryRequest requestDto);

    AccountCategoryResponse updateAccountCategory (UpdateAccountCategoryRequest requestDto);

    Optional<AccountCategoryResponse> getAccountCategoryById (UUID id);

    PaginationResponse<AccountCategoryResponse> getAccountCategories (UUID organizationId, PaginationRequest request);

}
