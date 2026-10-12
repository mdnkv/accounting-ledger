package dev.mednikov.accounting.ledger.journals.services;

import dev.mednikov.accounting.ledger.accounts.exceptions.AccountDoesNotExistException;
import dev.mednikov.accounting.ledger.accounts.models.Account;
import dev.mednikov.accounting.ledger.accounts.repositories.AccountRepository;
import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryLineRequest;
import dev.mednikov.accounting.ledger.journals.models.JournalEntry;
import dev.mednikov.accounting.ledger.journals.models.JournalEntryLine;
import dev.mednikov.accounting.ledger.journals.repositories.JournalEntryLineRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JournalEntryLineServiceImpl implements JournalEntryLineService{

    private final AccountRepository accountRepository;
    private final JournalEntryLineRepository journalEntryLineRepository;

    public JournalEntryLineServiceImpl(AccountRepository accountRepository, JournalEntryLineRepository journalEntryLineRepository) {
        this.accountRepository = accountRepository;
        this.journalEntryLineRepository = journalEntryLineRepository;
    }

    @Override
    public void createJournalEntryLines(JournalEntry journalEntry, List<CreateJournalEntryLineRequest> lines) {
        List<JournalEntryLine> journalEntryLines = new ArrayList<>();

        for (CreateJournalEntryLineRequest line : lines) {
            // validate account
            Account account = accountRepository.findById(line.accountId()).orElseThrow(() -> new AccountDoesNotExistException(line.accountId()));

            JournalEntryLine journalEntryLine = new JournalEntryLine();
            journalEntryLine.setJournalEntry(journalEntry);
            journalEntryLine.setAccount(account);

            journalEntryLine.setDebit(line.debit());
            journalEntryLine.setCredit(line.credit());

            journalEntryLines.add(journalEntryLine);
        }

        journalEntryLineRepository.saveAll(journalEntryLines);
    }
}
