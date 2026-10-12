package dev.mednikov.accounting.ledger.currencies.services;

import java.util.Optional;
import java.util.UUID;

import dev.mednikov.accounting.ledger.currencies.domain.CreateCurrencyRequest;
import dev.mednikov.accounting.ledger.currencies.domain.CurrencyResponse;
import dev.mednikov.accounting.ledger.currencies.domain.UpdateCurrencyRequest;

public interface CurrencyService {

    CurrencyResponse createCurrency (CreateCurrencyRequest requestDto);

    CurrencyResponse updateCurrency (UpdateCurrencyRequest requestDto);

    Optional<CurrencyResponse> getCurrencyById (UUID id);


}