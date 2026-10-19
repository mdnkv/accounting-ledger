package dev.mednikov.accounting.ledger.accounts.services;

import dev.mednikov.accounting.ledger.accounts.domain.AccountResponse;
import dev.mednikov.accounting.ledger.accounts.domain.CreateAccountRequest;
import dev.mednikov.accounting.ledger.accounts.domain.UpdateAccountRequest;
import dev.mednikov.accounting.ledger.accounts.exceptions.AccountAlreadyExistsException;
import dev.mednikov.accounting.ledger.accounts.exceptions.AccountCategoryDoesNotExistException;
import dev.mednikov.accounting.ledger.accounts.exceptions.AccountDoesNotExistException;
import dev.mednikov.accounting.ledger.accounts.mappers.AccountCategoryResponseMapper;
import dev.mednikov.accounting.ledger.accounts.mappers.AccountCategoryResponseMapperImpl;
import dev.mednikov.accounting.ledger.accounts.mappers.AccountResponseMapper;
import dev.mednikov.accounting.ledger.accounts.mappers.AccountResponseMapperImpl;
import dev.mednikov.accounting.ledger.accounts.models.Account;
import dev.mednikov.accounting.ledger.accounts.models.AccountBalanceType;
import dev.mednikov.accounting.ledger.accounts.models.AccountCategory;
import dev.mednikov.accounting.ledger.accounts.models.AccountType;
import dev.mednikov.accounting.ledger.accounts.repositories.AccountCategoryRepository;
import dev.mednikov.accounting.ledger.accounts.repositories.AccountRepository;
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
class AccountServiceImplTest {

    @Mock private AccountCategoryRepository accountCategoryRepository;
    @Mock private AccountRepository accountRepository;

    private AccountServiceImpl service;

    @BeforeEach
    void setup(){
        AccountCategoryResponseMapper accountCategoryResponseMapper = new AccountCategoryResponseMapperImpl();
        AccountResponseMapper accountResponseMapper = new AccountResponseMapperImpl(accountCategoryResponseMapper);
        this.service = new AccountServiceImpl(accountRepository, accountCategoryRepository, accountResponseMapper);
    }

    @Test
    void createAccount_accountCategoryDoesNotExistTest(){
        UUID accountCategoryId = UUID.randomUUID();
        String accountCode = "101";
        String accountName = "Cash";
        UUID organizationId = UUID.randomUUID();

        CreateAccountRequest requestDto = new CreateAccountRequest(
                organizationId,
                accountCategoryId,
                accountName,
                accountCode,
                AccountBalanceType.DEBIT
        );
        

        when(accountRepository.existsByCodeAndOrganizationId(accountCode, organizationId)).thenReturn(false);
        when(accountCategoryRepository.findById(accountCategoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createAccount(requestDto)).isInstanceOf(AccountCategoryDoesNotExistException.class);
    }

    @Test
    void createAccount_codeAlreadyExistsTest(){
        UUID accountCategoryId = UUID.randomUUID();
        String accountCode = "101";
        String accountName = "Cash";
        UUID organizationId = UUID.randomUUID();

        CreateAccountRequest requestDto = new CreateAccountRequest(
                organizationId,
                accountCategoryId,
                accountName,
                accountCode,
                AccountBalanceType.DEBIT
        );

        when(accountRepository.existsByCodeAndOrganizationId(accountCode, organizationId)).thenReturn(true);

        assertThatThrownBy(() -> service.createAccount(requestDto)).isInstanceOf(AccountAlreadyExistsException.class);
    }
    

    @Test
    void createAccount_createdTest(){
        UUID accountId = UUID.randomUUID();
        UUID accountCategoryId = UUID.randomUUID();
        String accountCode = "101";
        String accountName = "Cash";
        UUID organizationId = UUID.randomUUID();

        CreateAccountRequest requestDto = new CreateAccountRequest(
                organizationId,
                accountCategoryId,
                accountName,
                accountCode,
                AccountBalanceType.DEBIT
        );

        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setId(accountCategoryId);
        accountCategory.setName("Current assets");
        accountCategory.setAccountType(AccountType.ASSET);
        accountCategory.setOrganizationId(organizationId);
        accountCategory.setActive(true);

        Account account = new Account();
        account.setId(accountId);
        account.setAccountCategory(accountCategory);
        account.setCode(accountCode);
        account.setName(accountName);
        account.setBalanceType(AccountBalanceType.DEBIT);
        account.setOrganizationId(organizationId);
        account.setActive(true);

        when(accountCategoryRepository.findById(accountCategoryId)).thenReturn(Optional.of(accountCategory));
        when(accountRepository.existsByCodeAndOrganizationId(accountCode, organizationId)).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponse result = service.createAccount(requestDto);
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("code", accountCode)
                .hasFieldOrPropertyWithValue("name", accountName)
                .hasFieldOrPropertyWithValue("balanceType", AccountBalanceType.DEBIT)
                .hasFieldOrPropertyWithValue("id", accountId)
                .hasFieldOrPropertyWithValue("organizationId", organizationId)
                .hasFieldOrPropertyWithValue("active", true)
                .hasFieldOrProperty("accountCategory");
    }

    @Test
    void updateAccount_accountDoesNotExistTest(){
        UUID accountId = UUID.randomUUID();
        UUID accountCategoryId = UUID.randomUUID();
        String accountCode = "101";
        String accountName = "Cash";
        UUID organizationId = UUID.randomUUID();

        UpdateAccountRequest requestDto = new UpdateAccountRequest(
                accountId,
                organizationId,
                accountCategoryId,
                accountName,
                accountCode,
                AccountBalanceType.DEBIT,
                true
        );

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateAccount(requestDto)).isInstanceOf(AccountDoesNotExistException.class);

    }

    @Test
    void updateAccount_accountCategoryDoesNotExistTest(){
        UUID accountId = UUID.randomUUID();
        UUID accountCategoryId = UUID.randomUUID();
        String accountCode = "101";
        String accountName = "Cash";
        UUID organizationId = UUID.randomUUID();

        UpdateAccountRequest requestDto = new UpdateAccountRequest(
                accountId,
                organizationId,
                accountCategoryId,
                accountName,
                accountCode,
                AccountBalanceType.DEBIT,
                true
        );

        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setId(UUID.randomUUID());
        accountCategory.setName("Current assets");
        accountCategory.setAccountType(AccountType.ASSET);
        accountCategory.setOrganizationId(organizationId);

        Account account = new Account();
        account.setId(accountId);
        account.setAccountCategory(accountCategory);
        account.setCode("1001");
        account.setName(accountName);
        account.setBalanceType(AccountBalanceType.DEBIT);
        account.setOrganizationId(organizationId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.existsByCodeAndOrganizationId(accountCode, organizationId)).thenReturn(false);
        when(accountCategoryRepository.findById(accountCategoryId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateAccount(requestDto)).isInstanceOf(AccountCategoryDoesNotExistException.class);
    }


    @Test
    void updateAccount_codeAlreadyExistsTest(){
        UUID accountId = UUID.randomUUID();
        UUID accountCategoryId = UUID.randomUUID();
        String accountCode = "101";
        String accountName = "Cash";
        UUID organizationId = UUID.randomUUID();

        UpdateAccountRequest requestDto = new UpdateAccountRequest(
                accountId,
                organizationId,
                accountCategoryId,
                accountName,
                accountCode,
                AccountBalanceType.DEBIT,
                true
        );

        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setId(accountCategoryId);
        accountCategory.setName("Current assets");
        accountCategory.setAccountType(AccountType.ASSET);
        accountCategory.setOrganizationId(organizationId);
        accountCategory.setActive(true);

        Account account = new Account();
        account.setId(accountId);
        account.setAccountCategory(accountCategory);
        account.setCode("1001");
        account.setName(accountName);
        account.setBalanceType(AccountBalanceType.DEBIT);
        account.setOrganizationId(organizationId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.existsByCodeAndOrganizationId(accountCode, organizationId)).thenReturn(true);

        assertThatThrownBy(() -> service.updateAccount(requestDto)).isInstanceOf(AccountAlreadyExistsException.class);
    }

    @Test
    void updateAccount_updatedTest(){
        UUID accountId = UUID.randomUUID();
        UUID accountCategoryId = UUID.randomUUID();
        String accountCode = "101";
        String accountName = "Cash";
        UUID organizationId = UUID.randomUUID();

        UpdateAccountRequest requestDto = new UpdateAccountRequest(
                accountId,
                organizationId,
                accountCategoryId,
                accountName,
                accountCode,
                AccountBalanceType.DEBIT,
                true
        );

        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setId(accountCategoryId);
        accountCategory.setName("Current assets");
        accountCategory.setAccountType(AccountType.ASSET);
        accountCategory.setOrganizationId(organizationId);
        accountCategory.setActive(true);

        Account account = new Account();
        account.setId(accountId);
        account.setAccountCategory(accountCategory);
        account.setCode("1001");
        account.setName(accountName);
        account.setBalanceType(AccountBalanceType.DEBIT);
        account.setOrganizationId(organizationId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(accountRepository.existsByCodeAndOrganizationId(accountCode, organizationId)).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountResponse result = service.updateAccount(requestDto);
        assertThat(result).isNotNull();
    }

    @Test
    void getAccountsTest(){
        UUID organizationId = UUID.randomUUID();
        UUID accountCategoryId = UUID.randomUUID();

        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setId(accountCategoryId);
        accountCategory.setName("Current assets");
        accountCategory.setAccountType(AccountType.ASSET);
        accountCategory.setOrganizationId(organizationId);
        accountCategory.setActive(true);

        List<Account> accounts = new ArrayList<>();

        Account account1 = new Account();
        account1.setOrganizationId(organizationId);
        account1.setBalanceType(AccountBalanceType.DEBIT);
        account1.setAccountCategory(accountCategory);
        account1.setActive(true);
        account1.setName("Cash");
        account1.setCode("10100");
        account1.setId(UUID.randomUUID());
        accounts.add(account1);

        Account account2 = new Account();
        account2.setOrganizationId(organizationId);
        account2.setBalanceType(AccountBalanceType.DEBIT);
        account2.setAccountCategory(accountCategory);
        account2.setActive(true);
        account2.setName("Accounts Receivable");
        account2.setCode("12100");
        account2.setId(UUID.randomUUID());
        accounts.add(account2);

        String sortBy = "name-asc";
        Pageable pageable = PageRequest.of(1, 10, Sort.by("name").ascending());
        Page<Account> page = new PageImpl<>(accounts, pageable, 30);

        SortingOrder sortingOrder = new SortingOrder(sortBy);
        PaginationRequest request = new PaginationRequest(sortingOrder, 1, 10);

        when(accountRepository.findAllByOrganizationId(any(), any())).thenReturn(page);

        PaginationResponse<AccountResponse> result = service.getAccounts(organizationId, request);
        assertThat(result).isNotNull()
                .hasFieldOrProperty("data")
                .hasFieldOrPropertyWithValue("totalPages", 3)
                .hasFieldOrPropertyWithValue("totalCount", 30L);

        assertThat(result.data()).hasSize(accounts.size());

    }

    @Test
    void getAccountById_existsTest(){
        UUID accountId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        AccountCategory accountCategory = new AccountCategory();
        accountCategory.setId(UUID.randomUUID());
        accountCategory.setName("Current liabilities");
        accountCategory.setAccountType(AccountType.LIABILITY);
        accountCategory.setOrganizationId(organizationId);

        Account account = new Account();
        account.setId(accountId);
        account.setAccountCategory(accountCategory);
        account.setCode("201");
        account.setName("Accounts Payable");
        account.setBalanceType(AccountBalanceType.CREDIT);
        account.setOrganizationId(organizationId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        Optional<AccountResponse> result = service.getAccountById(accountId);
        assertThat(result).isPresent();
    }

    @Test
    void getAccountById_doesNotExistTest(){
        UUID accountId = UUID.randomUUID();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        Optional<AccountResponse> result = service.getAccountById(accountId);
        assertThat(result).isEmpty();
    }
}
