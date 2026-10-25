package dev.mednikov.accounting.ledger.periods.services;

import dev.mednikov.accounting.ledger.periods.domain.CreatePeriodRequest;
import dev.mednikov.accounting.ledger.periods.domain.PeriodResponse;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;

import java.util.UUID;

public interface PeriodService {

    PeriodResponse createPeriod (CreatePeriodRequest request);

    PaginationResponse<PeriodResponse> getPeriods (UUID organizationId, PaginationRequest request);


}
