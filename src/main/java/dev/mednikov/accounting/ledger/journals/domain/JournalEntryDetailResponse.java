package dev.mednikov.accounting.ledger.journals.domain;

import java.util.List;

public record JournalEntryDetailResponse(
        JournalEntryResponse journalEntry,
        List<JournalEntryLineResponse> journalEntryLines
) {
}
