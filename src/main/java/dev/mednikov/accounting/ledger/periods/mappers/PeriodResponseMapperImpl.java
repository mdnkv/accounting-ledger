package dev.mednikov.accounting.ledger.periods.mappers;

import dev.mednikov.accounting.ledger.periods.domain.PeriodResponse;
import dev.mednikov.accounting.ledger.periods.models.Period;
import org.springframework.stereotype.Component;

@Component
public final class PeriodResponseMapperImpl implements PeriodResponseMapper{

    @Override
    public PeriodResponse apply(Period period) {
        return new PeriodResponse(
                period.getId(),
                period.getOrganizationId(),
                period.getPeriodStart(),
                period.getPeriodEnd(),
                period.isClosed()
        );
    }

}
