package dev.mednikov.accounting.ledger.accounts.services;

import dev.mednikov.accounting.ledger.accounts.domain.AccountCategoryResponse;
import dev.mednikov.accounting.ledger.accounts.domain.CreateAccountCategoryRequest;
import dev.mednikov.accounting.ledger.accounts.domain.UpdateAccountCategoryRequest;
import dev.mednikov.accounting.ledger.accounts.exceptions.AccountCategoryAlreadyExistsException;
import dev.mednikov.accounting.ledger.accounts.exceptions.AccountCategoryDoesNotExistException;
import dev.mednikov.accounting.ledger.accounts.mappers.AccountCategoryResponseMapper;
import dev.mednikov.accounting.ledger.accounts.models.AccountCategory;
import dev.mednikov.accounting.ledger.accounts.repositories.AccountCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountCategoryServiceImpl implements AccountCategoryService{

    private final AccountCategoryRepository accountCategoryRepository;
    private final AccountCategoryResponseMapper mapper;

    public AccountCategoryServiceImpl(AccountCategoryRepository accountCategoryRepository, AccountCategoryResponseMapper mapper) {
        this.accountCategoryRepository = accountCategoryRepository;
        this.mapper = mapper;
    }

    @Override
    public AccountCategoryResponse createAccountCategory(CreateAccountCategoryRequest request) {
        // Check that the name is not used
        String name = request.name();
        if (this.accountCategoryRepository.existsByNameAndOrganizationId(name, request.organizationId())) {
            throw new AccountCategoryAlreadyExistsException(request.organizationId(), name);
        }

        // Create account category
        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setName(name);
        accountCategory.setOrganizationId(request.organizationId());
        accountCategory.setAccountType(request.accountType());
        accountCategory.setActive(true);

        // Persist
        AccountCategory result = this.accountCategoryRepository.save(accountCategory);

        // Return result
        return this.mapper.apply(result);
    }

    @Override
    public AccountCategoryResponse updateAccountCategory(UpdateAccountCategoryRequest request) {
        // Find account category
        AccountCategory accountCategory = this.accountCategoryRepository.findById(request.id())
                .orElseThrow(() -> new AccountCategoryDoesNotExistException(request.id()));

        // If name is changed, check that it is not used
        String newName = request.name();
        if (!newName.equals(accountCategory.getName())) {
            if (this.accountCategoryRepository.existsByNameAndOrganizationId(newName, request.organizationId())) {
                throw new AccountCategoryAlreadyExistsException(request.organizationId(), newName);
            }
            accountCategory.setName(newName);
        }

        // Update other fields
        accountCategory.setAccountType(request.accountType());
        accountCategory.setActive(request.active());

        // Persist
        AccountCategory result = this.accountCategoryRepository.save(accountCategory);

        // Return result
        return this.mapper.apply(result);
    }

    @Override
    public Optional<AccountCategoryResponse> getAccountCategoryById(UUID id) {
        return this.accountCategoryRepository.findById(id).map(this.mapper);
    }
}
