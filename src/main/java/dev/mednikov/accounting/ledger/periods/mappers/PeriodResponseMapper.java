package dev.mednikov.accounting.ledger.periods.mappers;

import dev.mednikov.accounting.ledger.periods.domain.PeriodResponse;
import dev.mednikov.accounting.ledger.periods.models.Period;

import java.util.function.Function;

public interface PeriodResponseMapper extends Function<Period, PeriodResponse> {
}
