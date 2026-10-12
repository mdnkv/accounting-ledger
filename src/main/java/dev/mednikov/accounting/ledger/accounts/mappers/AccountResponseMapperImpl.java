package dev.mednikov.accounting.ledger.accounts.mappers;

import dev.mednikov.accounting.ledger.accounts.domain.AccountCategoryResponse;
import dev.mednikov.accounting.ledger.accounts.domain.AccountResponse;
import dev.mednikov.accounting.ledger.accounts.models.Account;
import org.springframework.stereotype.Component;

@Component
public final class AccountResponseMapperImpl implements AccountResponseMapper {

    private final AccountCategoryResponseMapper accountCategoryResponseMapper;

    public AccountResponseMapperImpl(AccountCategoryResponseMapper accountCategoryResponseMapper) {
        this.accountCategoryResponseMapper = accountCategoryResponseMapper;
    }

    @Override
    public AccountResponse apply(Account account) {
        AccountCategoryResponse accountCategory = this.accountCategoryResponseMapper.apply(account.getAccountCategory());
        return new AccountResponse(
                account.getId(),
                account.getOrganizationId(),
                accountCategory,
                account.getName(),
                account.getCode(),
                account.getBalanceType(),
                account.isActive()
        );
    }
}
