package dev.mednikov.accounting.ledger.journals.mappers;

import dev.mednikov.accounting.ledger.journals.domain.JournalEntryResponse;
import dev.mednikov.accounting.ledger.journals.models.JournalEntry;

import java.util.function.Function;

public interface JournalEntryResponseMapper extends Function<JournalEntry, JournalEntryResponse> {
}
