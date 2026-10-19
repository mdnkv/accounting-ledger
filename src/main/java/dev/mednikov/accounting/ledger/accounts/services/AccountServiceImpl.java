package dev.mednikov.accounting.ledger.accounts.services;

import dev.mednikov.accounting.ledger.accounts.domain.AccountResponse;
import dev.mednikov.accounting.ledger.accounts.domain.CreateAccountRequest;
import dev.mednikov.accounting.ledger.accounts.domain.UpdateAccountRequest;
import dev.mednikov.accounting.ledger.accounts.exceptions.AccountAlreadyExistsException;
import dev.mednikov.accounting.ledger.accounts.exceptions.AccountCategoryDoesNotExistException;
import dev.mednikov.accounting.ledger.accounts.exceptions.AccountDoesNotExistException;
import dev.mednikov.accounting.ledger.accounts.mappers.AccountResponseMapper;
import dev.mednikov.accounting.ledger.accounts.models.Account;
import dev.mednikov.accounting.ledger.accounts.models.AccountCategory;
import dev.mednikov.accounting.ledger.accounts.repositories.AccountCategoryRepository;
import dev.mednikov.accounting.ledger.accounts.repositories.AccountRepository;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountCategoryRepository accountCategoryRepository;
    private final AccountResponseMapper mapper;

    public AccountServiceImpl(AccountRepository accountRepository, AccountCategoryRepository accountCategoryRepository, AccountResponseMapper mapper) {
        this.accountRepository = accountRepository;
        this.accountCategoryRepository = accountCategoryRepository;
        this.mapper = mapper;
    }

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        // Check that the code is not used
        String accountCode = request.code();
        if (this.accountRepository.existsByCodeAndOrganizationId(accountCode, request.organizationId())) {
            throw new AccountAlreadyExistsException(request.organizationId(), accountCode);
        }

        // Find account category
        UUID accountCategoryId = request.accountCategoryId();
        AccountCategory category = this.accountCategoryRepository.findById(accountCategoryId)
                .orElseThrow(() -> new AccountCategoryDoesNotExistException(accountCategoryId));

        // Create new account
        Account account = new Account();
        account.setAccountCategory(category);
        account.setCode(accountCode);
        account.setName(request.name());
        account.setBalanceType(request.balanceType());
        account.setOrganizationId(request.organizationId());
        account.setActive(true);

        // Persist
        Account result = this.accountRepository.save(account);

        // TODO emit AccountCreatedEvent

        // Return
        return this.mapper.apply(result);
    }

    @Override
    public AccountResponse updateAccount(UpdateAccountRequest request) {
        // Find account or throw exception
        Account account = this.accountRepository.findById(request.id())
                .orElseThrow(() -> new AccountDoesNotExistException(request.id()));

        // Check that account code is not occupied
        String newCode = request.code();
        if (!account.getCode().equals(newCode)) {
            if (this.accountRepository.existsByCodeAndOrganizationId(newCode, request.organizationId())) {
                throw new AccountAlreadyExistsException(request.organizationId(), newCode);
            }
            account.setCode(newCode);
        }

        // Update account category if needed
        if (!account.getAccountCategory().getId().equals(request.accountCategoryId())) {
            AccountCategory newCategory = this.accountCategoryRepository.findById(request.accountCategoryId())
                    .orElseThrow(() -> new AccountCategoryDoesNotExistException(request.accountCategoryId()));
            account.setAccountCategory(newCategory);
        }

        // Update other fields
        account.setName(request.name());
        account.setBalanceType(request.balanceType());
        account.setActive(request.active());

        // Persist
        Account result = this.accountRepository.save(account);

        // TODO emit AccountUpdatedEvent

        // Return
        return this.mapper.apply(result);
    }

    @Override
    public PaginationResponse<AccountResponse> getAccounts(UUID organizationId, PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.count(), request.sortingOrder().getSort());
        Page<Account> result = this.accountRepository.findAllByOrganizationId(organizationId, pageable);
        List<AccountResponse> data = result.map(this.mapper).stream().toList();
        return new PaginationResponse<>(data, result.getTotalPages(), result.getTotalElements());
    }

    @Override
    public Optional<AccountResponse> getAccountById(UUID id) {
        return this.accountRepository.findById(id).map(this.mapper);
    }

}
