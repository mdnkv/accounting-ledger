package dev.mednikov.accounting.ledger.journals.mappers;

import dev.mednikov.accounting.ledger.journals.domain.JournalEntryLineResponse;
import dev.mednikov.accounting.ledger.journals.models.JournalEntryLine;

import java.util.function.Function;

public interface JournalEntryLineResponseMapper extends Function<JournalEntryLine, JournalEntryLineResponse> {
}
