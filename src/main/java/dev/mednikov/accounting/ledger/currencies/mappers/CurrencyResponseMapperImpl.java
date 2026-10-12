package dev.mednikov.accounting.ledger.currencies.mappers;

import dev.mednikov.accounting.ledger.currencies.domain.CurrencyResponse;
import dev.mednikov.accounting.ledger.currencies.models.Currency;
import org.springframework.stereotype.Component;

@Component
public final class CurrencyResponseMapperImpl implements CurrencyResponseMapper {

    @Override
    public CurrencyResponse apply(Currency currency) {
        return new CurrencyResponse(
                currency.getId(),
                currency.getOrganizationId(),
                currency.getName(),
                currency.getCurrencyCode(),
                currency.isPrimary(),
                currency.isActive()
        );
    }

}
