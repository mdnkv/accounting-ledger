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
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;
import dev.mednikov.accounting.ledger.shared.domain.SortingOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.ArrayList;
import java.util.List;
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
    void getAccountCategoriesTest(){
        List<AccountCategory> accountCategories = new ArrayList<>();
        UUID organizationId = UUID.randomUUID();

        AccountCategory accountCategory1 = new AccountCategory();
        accountCategory1.setId(UUID.randomUUID());
        accountCategory1.setName("Current assets");
        accountCategory1.setAccountType(AccountType.ASSET);
        accountCategory1.setOrganizationId(organizationId);
        accountCategories.add(accountCategory1);

        AccountCategory accountCategory2 = new AccountCategory();
        accountCategory2.setId(UUID.randomUUID());
        accountCategory2.setName("Non current assets");
        accountCategory2.setAccountType(AccountType.ASSET);
        accountCategory2.setOrganizationId(organizationId);
        accountCategories.add(accountCategory2);

        AccountCategory accountCategory3 = new AccountCategory();
        accountCategory3.setId(UUID.randomUUID());
        accountCategory3.setName("Current Liabilities");
        accountCategory3.setAccountType(AccountType.LIABILITY);
        accountCategory3.setOrganizationId(organizationId);
        accountCategories.add(accountCategory3);

        String sortBy = "name-asc";
        Pageable pageable = PageRequest.of(1, 10, Sort.by("name").ascending());
        Page<AccountCategory> page = new PageImpl<>(accountCategories, pageable, 50);

        SortingOrder sortingOrder = new SortingOrder(sortBy);
        PaginationRequest request = new PaginationRequest(sortingOrder, 1, 10);

        when(accountCategoryRepository.findAllByOrganizationId(any(), any())).thenReturn(page);

        PaginationResponse<AccountCategoryResponse> result = service.getAccountCategories(organizationId, request);
        assertThat(result).isNotNull()
                .hasFieldOrProperty("data")
                .hasFieldOrPropertyWithValue("totalPages", 5)
                .hasFieldOrPropertyWithValue("totalCount", 50L);

        assertThat(result.data()).hasSize(accountCategories.size());

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
