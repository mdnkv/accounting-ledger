package dev.mednikov.accounting.ledger.accounts.mappers;

import dev.mednikov.accounting.ledger.accounts.domain.AccountCategoryResponse;
import dev.mednikov.accounting.ledger.accounts.models.AccountCategory;
import org.springframework.stereotype.Component;

@Component
public final class AccountCategoryResponseMapperImpl implements AccountCategoryResponseMapper {

    @Override
    public AccountCategoryResponse apply(AccountCategory accountCategory) {
        return new AccountCategoryResponse(
                accountCategory.getId(),
                accountCategory.getOrganizationId(),
                accountCategory.getName(),
                accountCategory.getAccountType(),
                accountCategory.isActive()
        );
    }


}
