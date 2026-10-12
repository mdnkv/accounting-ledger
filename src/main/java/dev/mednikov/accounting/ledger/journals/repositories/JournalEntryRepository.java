package dev.mednikov.accounting.ledger.journals.repositories;

import dev.mednikov.accounting.ledger.journals.models.JournalEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JournalEntryRepository extends JpaRepository<JournalEntry, UUID> {
}
