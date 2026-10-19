package dev.mednikov.accounting.ledger.journals.services;

import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryRequest;
import dev.mednikov.accounting.ledger.journals.domain.JournalEntryResponse;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;

import java.util.UUID;

public interface JournalEntryService {

    JournalEntryResponse createJournalEntry (CreateJournalEntryRequest request);

    PaginationResponse<JournalEntryResponse> getJournalEntries (UUID organizationId, PaginationRequest request);

}
