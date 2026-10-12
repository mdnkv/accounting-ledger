package dev.mednikov.accounting.ledger.accounts.services;

import dev.mednikov.accounting.ledger.accounts.domain.AccountCategoryResponse;
import dev.mednikov.accounting.ledger.accounts.domain.CreateAccountCategoryRequest;
import dev.mednikov.accounting.ledger.accounts.domain.UpdateAccountCategoryRequest;
import dev.mednikov.accounting.ledger.accounts.exceptions.AccountCategoryAlreadyExistsException;
import dev.mednikov.accounting.ledger.accounts.exceptions.AccountCategoryDoesNotExistException;
import dev.mednikov.accounting.ledger.accounts.mappers.AccountCategoryResponseMapperImpl;
import dev.mednikov.accounting.ledger.accounts.models.AccountCategory;
import dev.mednikov.accounting.ledger.accounts.models.AccountType;
import dev.mednikov.accounting.ledger.accounts.repositories.AccountCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountCategoryServiceImplTest {

    @Mock private AccountCategoryRepository accountCategoryRepository;

    private AccountCategoryServiceImpl service;

    @BeforeEach
    void setup(){
        this.service = new AccountCategoryServiceImpl(accountCategoryRepository, new AccountCategoryResponseMapperImpl());
    }

    @Test
    void createAccountCategory_nameAlreadyExistsTest(){
        String name = "Current assets";
        UUID organizationId = UUID.randomUUID();

        CreateAccountCategoryRequest request = new CreateAccountCategoryRequest(organizationId, name, AccountType.ASSET);

        when(accountCategoryRepository.existsByNameAndOrganizationId(name, organizationId)).thenReturn(true);

        assertThatThrownBy(()-> service.createAccountCategory(request)).isInstanceOf(AccountCategoryAlreadyExistsException.class);
    }

    @Test
    void createAccountCategory_createdTest(){
        String name = "Current assets";
        UUID accountCategoryId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        CreateAccountCategoryRequest request = new  CreateAccountCategoryRequest(organizationId, name, AccountType.ASSET);

        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setId(accountCategoryId);
        accountCategory.setName(name);
        accountCategory.setAccountType(AccountType.ASSET);
        accountCategory.setOrganizationId(organizationId);
        accountCategory.setActive(true);

        when(accountCategoryRepository.existsByNameAndOrganizationId(name, organizationId)).thenReturn(false);
        when(accountCategoryRepository.save(any(AccountCategory.class))).thenReturn(accountCategory);

        AccountCategoryResponse result = service.createAccountCategory(request);
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("id", accountCategoryId)
                .hasFieldOrPropertyWithValue("name", name)
                .hasFieldOrPropertyWithValue("organizationId", organizationId)
                .hasFieldOrPropertyWithValue("active", true)
                .hasFieldOrPropertyWithValue("accountType", AccountType.ASSET);
    }

    @Test
    void updateAccountCategory_accountCategoryDoesNotExistTest(){
        String name = "Current assets";
        UUID accountCategoryId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        UpdateAccountCategoryRequest request = new UpdateAccountCategoryRequest(accountCategoryId, organizationId, name, AccountType.ASSET, true);

        when(accountCategoryRepository.findById(accountCategoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(()-> service.updateAccountCategory(request)).isInstanceOf(AccountCategoryDoesNotExistException.class);

    }

    @Test
    void updateAccountCategory_nameAlreadyExistsTest(){
        String name = "Current assets";
        UUID accountCategoryId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        UpdateAccountCategoryRequest request = new UpdateAccountCategoryRequest(accountCategoryId, organizationId, name, AccountType.ASSET, true);

        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setId(accountCategoryId);
        accountCategory.setName("Property, Plant, and Equipment");
        accountCategory.setAccountType(AccountType.ASSET);
        accountCategory.setOrganizationId(organizationId);

        when(accountCategoryRepository.findById(accountCategoryId)).thenReturn(Optional.of(accountCategory));
        when(accountCategoryRepository.existsByNameAndOrganizationId(name, organizationId)).thenReturn(true);

        assertThatThrownBy(()-> service.updateAccountCategory(request)).isInstanceOf(AccountCategoryAlreadyExistsException.class);
    }

    @Test
    void updateAccountCategory_updatedTest(){
        String name = "Current assets";
        UUID accountCategoryId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        UpdateAccountCategoryRequest request = new UpdateAccountCategoryRequest(accountCategoryId, organizationId, name, AccountType.ASSET, true);

        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setId(accountCategoryId);
        accountCategory.setName("Property, Plant, and Equipment");
        accountCategory.setAccountType(AccountType.ASSET);
        accountCategory.setOrganizationId(organizationId);

        when(accountCategoryRepository.findById(accountCategoryId)).thenReturn(Optional.of(accountCategory));
        when(accountCategoryRepository.existsByNameAndOrganizationId(name, organizationId)).thenReturn(false);
        when(accountCategoryRepository.save(any(AccountCategory.class))).thenReturn(accountCategory);

        AccountCategoryResponse result = service.updateAccountCategory(request);
        assertThat(result).isNotNull();
    }

    @Test
    void getAccountCategoryById_existsTest(){
        UUID accountCategoryId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setId(accountCategoryId);
        accountCategory.setName("Current Liabilities");
        accountCategory.setAccountType(AccountType.LIABILITY);
        accountCategory.setOrganizationId(organizationId);

        when(accountCategoryRepository.findById(accountCategoryId)).thenReturn(Optional.of(accountCategory));

        Optional<AccountCategoryResponse> result = service.getAccountCategoryById(accountCategoryId);
        assertThat(result).isPresent();
    }

    @Test
    void getAccountCategoryById_doesNotExistTest(){
        UUID accountCategoryId = UUID.randomUUID();

        when(accountCategoryRepository.findById(accountCategoryId)).thenReturn(Optional.empty());
        Optional<AccountCategoryResponse> result = service.getAccountCategoryById(accountCategoryId);
        assertThat(result).isEmpty();
    }


}
