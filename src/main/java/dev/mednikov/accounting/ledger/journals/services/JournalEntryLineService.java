package dev.mednikov.accounting.ledger.journals.services;

import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryLineRequest;
import dev.mednikov.accounting.ledger.journals.domain.JournalEntryLineResponse;
import dev.mednikov.accounting.ledger.journals.models.JournalEntry;

import java.util.List;
import java.util.UUID;

public interface JournalEntryLineService {

    void createJournalEntryLines (JournalEntry journalEntry, List<CreateJournalEntryLineRequest> lines);

}
