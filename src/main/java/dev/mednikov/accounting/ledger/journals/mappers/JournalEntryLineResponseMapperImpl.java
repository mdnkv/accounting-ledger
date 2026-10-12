package dev.mednikov.accounting.ledger.journals.mappers;

import dev.mednikov.accounting.ledger.accounts.domain.AccountResponse;
import dev.mednikov.accounting.ledger.accounts.mappers.AccountResponseMapper;
import dev.mednikov.accounting.ledger.journals.domain.JournalEntryLineResponse;
import dev.mednikov.accounting.ledger.journals.models.JournalEntryLine;
import org.springframework.stereotype.Component;

@Component
public final class JournalEntryLineResponseMapperImpl implements JournalEntryLineResponseMapper  {

    private final AccountResponseMapper accountResponseMapper;

    public JournalEntryLineResponseMapperImpl(AccountResponseMapper accountResponseMapper) {
        this.accountResponseMapper = accountResponseMapper;
    }

    @Override
    public JournalEntryLineResponse apply(JournalEntryLine journalEntryLine) {
        AccountResponse accountResponse = this.accountResponseMapper.apply(journalEntryLine.getAccount());
        return new JournalEntryLineResponse(
                journalEntryLine.getId(),
                accountResponse,
                journalEntryLine.getDebit(),
                journalEntryLine.getCredit(),
                journalEntryLine.getPosition()
        );
    }
}
