package dev.mednikov.accounting.ledger.periods.services;

import dev.mednikov.accounting.ledger.periods.domain.CreatePeriodRequest;
import dev.mednikov.accounting.ledger.periods.domain.PeriodResponse;
import dev.mednikov.accounting.ledger.periods.exceptions.PeriodAlreadyExistsException;
import dev.mednikov.accounting.ledger.periods.mappers.PeriodResponseMapper;
import dev.mednikov.accounting.ledger.periods.models.Period;
import dev.mednikov.accounting.ledger.periods.repositories.PeriodRepository;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class PeriodServiceImpl implements PeriodService{

    private final PeriodRepository periodRepository;
    private final PeriodResponseMapper mapper;


    public PeriodServiceImpl(PeriodRepository periodRepository, PeriodResponseMapper mapper) {
        this.periodRepository = periodRepository;
        this.mapper = mapper;
    }

    @Override
    public PeriodResponse createPeriod(CreatePeriodRequest request) {
        LocalDate periodStart = request.periodStart();
        LocalDate periodEnd = request.periodEnd();
        UUID organizationId = request.organizationId();

        if (this.periodRepository.existsByOrganizationIdAndPeriodStartAndPeriodEnd(organizationId, periodStart, periodEnd)) {
            throw new PeriodAlreadyExistsException(organizationId, periodStart, periodEnd);
        }


        Period period = new Period();
        period.setPeriodStart(periodStart);
        period.setPeriodEnd(periodEnd);
        period.setOrganizationId(organizationId);
        period.setClosed(false);
        period.setClosingJournalEntry(null);

        Period result = this.periodRepository.save(period);
        return mapper.apply(result);
    }

    @Override
    public PaginationResponse<PeriodResponse> getPeriods(UUID organizationId, PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.count(), request.sortingOrder().getSort());
        Page<Period> result = this.periodRepository.findAllByOrganizationId(organizationId, pageable);
        List<PeriodResponse> data = result.map(this.mapper).stream().toList();
        return new PaginationResponse<>(data, result.getTotalPages(), result.getTotalElements());
    }
}
