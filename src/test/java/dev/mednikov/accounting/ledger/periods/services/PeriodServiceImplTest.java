package dev.mednikov.accounting.ledger.periods.services;

import dev.mednikov.accounting.ledger.periods.domain.CreatePeriodRequest;
import dev.mednikov.accounting.ledger.periods.domain.PeriodResponse;
import dev.mednikov.accounting.ledger.periods.exceptions.PeriodAlreadyExistsException;
import dev.mednikov.accounting.ledger.periods.mappers.PeriodResponseMapper;
import dev.mednikov.accounting.ledger.periods.mappers.PeriodResponseMapperImpl;
import dev.mednikov.accounting.ledger.periods.models.Period;
import dev.mednikov.accounting.ledger.periods.repositories.PeriodRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeriodServiceImplTest {

    @Mock private PeriodRepository periodRepository;

    private PeriodServiceImpl service;

    @BeforeEach
    void setup(){
        PeriodResponseMapper mapper = new PeriodResponseMapperImpl();
        this.service = new PeriodServiceImpl(periodRepository, mapper);
    }

    @Test
    void createPeriod_periodAlreadyExistsTest(){
        UUID organizationId = UUID.randomUUID();
        LocalDate periodStart = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
        LocalDate periodEnd = LocalDate.now().with(TemporalAdjusters.lastDayOfYear());

        CreatePeriodRequest request = new CreatePeriodRequest(organizationId, periodStart, periodEnd);

        when(periodRepository.existsByOrganizationIdAndPeriodStartAndPeriodEnd(organizationId, periodStart, periodEnd)).thenReturn(true);
        assertThatThrownBy(() -> service.createPeriod(request)).isInstanceOf(PeriodAlreadyExistsException.class);
    }

    @Test
    void createPeriod_createdTest(){
        UUID organizationId = UUID.randomUUID();
        UUID periodId = UUID.randomUUID();
        LocalDate periodStart = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
        LocalDate periodEnd = LocalDate.now().with(TemporalAdjusters.lastDayOfYear());

        CreatePeriodRequest request = new CreatePeriodRequest(organizationId, periodStart, periodEnd);

        Period period = new Period();
        period.setId(periodId);
        period.setOrganizationId(organizationId);
        period.setClosed(false);
        period.setPeriodStart(periodStart);
        period.setPeriodEnd(periodEnd);

        when(periodRepository.existsByOrganizationIdAndPeriodStartAndPeriodEnd(organizationId, periodStart, periodEnd)).thenReturn(false);
        when(periodRepository.save(any(Period.class))).thenReturn(period);

        PeriodResponse result = service.createPeriod(request);
        assertThat(result).isNotNull()
                .hasFieldOrPropertyWithValue("id", periodId)
                .hasFieldOrPropertyWithValue("organizationId", organizationId)
                .hasFieldOrPropertyWithValue("periodStart", periodStart)
                .hasFieldOrPropertyWithValue("periodEnd", periodEnd)
                .hasFieldOrPropertyWithValue("closed", false);

    }

}
