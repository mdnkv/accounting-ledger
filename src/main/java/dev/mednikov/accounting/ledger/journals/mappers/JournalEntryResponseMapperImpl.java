package dev.mednikov.accounting.ledger.journals.mappers;

import dev.mednikov.accounting.ledger.currencies.mappers.CurrencyResponseMapper;
import dev.mednikov.accounting.ledger.currencies.domain.CurrencyResponse;
import dev.mednikov.accounting.ledger.journals.domain.JournalEntryResponse;
import dev.mednikov.accounting.ledger.journals.models.JournalEntry;
import org.springframework.stereotype.Component;

@Component
public final class JournalEntryResponseMapperImpl implements JournalEntryResponseMapper {

    private final CurrencyResponseMapper currencyResponseMapper;

    public JournalEntryResponseMapperImpl(CurrencyResponseMapper currencyResponseMapper) {
        this.currencyResponseMapper = currencyResponseMapper;
    }

    @Override
    public JournalEntryResponse apply(JournalEntry journalEntry) {
        CurrencyResponse currencyResponse = this.currencyResponseMapper.apply(journalEntry.getCurrency());
        return new JournalEntryResponse(
                journalEntry.getId(),
                journalEntry.getOrganizationId(),
                journalEntry.getJournalEntryType(),
                currencyResponse,
                journalEntry.getDescription(),
                journalEntry.getDate(),
                journalEntry.getTotalDebit(),
                journalEntry.getTotalCredit()
        );
    }
}
