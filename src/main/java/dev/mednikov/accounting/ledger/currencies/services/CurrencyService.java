package dev.mednikov.accounting.ledger.currencies.services;

import java.util.Optional;
import java.util.UUID;

import dev.mednikov.accounting.ledger.currencies.domain.CreateCurrencyRequest;
import dev.mednikov.accounting.ledger.currencies.domain.CurrencyResponse;
import dev.mednikov.accounting.ledger.currencies.domain.UpdateCurrencyRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;

public interface CurrencyService {

    CurrencyResponse createCurrency (CreateCurrencyRequest requestDto);

    CurrencyResponse updateCurrency (UpdateCurrencyRequest requestDto);

    PaginationResponse<CurrencyResponse> getCurrencies (UUID organizationId, PaginationRequest request);

    Optional<CurrencyResponse> getCurrencyById (UUID id);


}