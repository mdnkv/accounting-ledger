package dev.mednikov.accounting.ledger.journals.services;

import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryRequest;
import dev.mednikov.accounting.ledger.journals.domain.JournalEntryResponse;

public interface JournalEntryService {

    JournalEntryResponse createJournalEntry (CreateJournalEntryRequest request);

}
