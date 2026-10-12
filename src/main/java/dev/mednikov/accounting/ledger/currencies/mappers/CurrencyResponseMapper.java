package dev.mednikov.accounting.ledger.currencies.mappers;

import dev.mednikov.accounting.ledger.currencies.domain.CurrencyResponse;
import dev.mednikov.accounting.ledger.currencies.models.Currency;

import java.util.function.Function;

public interface CurrencyResponseMapper extends Function<Currency, CurrencyResponse> {
}
