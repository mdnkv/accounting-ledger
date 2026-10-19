package dev.mednikov.accounting.ledger.journals.services;

import dev.mednikov.accounting.ledger.currencies.exceptions.CurrencyDoesNotExistException;
import dev.mednikov.accounting.ledger.currencies.models.Currency;
import dev.mednikov.accounting.ledger.currencies.repositories.CurrencyRepository;
import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryLineRequest;
import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryRequest;
import dev.mednikov.accounting.ledger.journals.domain.JournalEntryResponse;
import dev.mednikov.accounting.ledger.journals.exceptions.NotBalancedEntryException;
import dev.mednikov.accounting.ledger.journals.mappers.JournalEntryResponseMapper;
import dev.mednikov.accounting.ledger.journals.models.JournalEntry;
import dev.mednikov.accounting.ledger.journals.repositories.JournalEntryRepository;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class JournalEntryServiceImpl implements JournalEntryService {

    private final JournalEntryLineService journalEntryLineService;
    private final JournalEntryRepository journalEntryRepository;
    private final CurrencyRepository currencyRepository;
    private final JournalEntryResponseMapper mapper;

    public JournalEntryServiceImpl(
            JournalEntryLineService journalEntryLineService,
            JournalEntryRepository journalEntryRepository,
            CurrencyRepository currencyRepository,
            JournalEntryResponseMapper mapper) {
        this.journalEntryLineService = journalEntryLineService;
        this.journalEntryRepository = journalEntryRepository;
        this.currencyRepository = currencyRepository;
        this.mapper = mapper;
    }

    @Override
    public JournalEntryResponse createJournalEntry(CreateJournalEntryRequest request) {
        // Calculate debit and credit
        BigDecimal totalCredit = request.journalEntryLines().stream()
                .map(CreateJournalEntryLineRequest::credit).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDebit = request.journalEntryLines().stream()
                .map(CreateJournalEntryLineRequest::debit).reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalCredit.compareTo(totalDebit) != 0) {
            throw new NotBalancedEntryException();
        }

        Currency currency = this.currencyRepository.findById(request.currencyId())
                .orElseThrow(() -> new CurrencyDoesNotExistException(request.currencyId()));

        // Create journal entry
        JournalEntry journalEntry = new JournalEntry();
        journalEntry.setCurrency(currency);
        journalEntry.setTotalDebit(totalDebit);
        journalEntry.setTotalCredit(totalCredit);
        journalEntry.setDate(request.date());
        journalEntry.setDescription(request.description());
        journalEntry.setOrganizationId(request.organizationId());
        journalEntry.setJournalEntryType(request.journalEntryType());

        JournalEntry result = this.journalEntryRepository.save(journalEntry);

        // Calculate journal entry lines
        this.journalEntryLineService.createJournalEntryLines(result, request.journalEntryLines());

        // Return result
        return this.mapper.apply(result);
    }

    @Override
    public PaginationResponse<JournalEntryResponse> getJournalEntries(UUID organizationId, PaginationRequest request) {
        Pageable pageable = PageRequest.of(request.page() - 1, request.count(), request.sortingOrder().getSort());
        Page<JournalEntry> result = this.journalEntryRepository.findAllByOrganizationId(organizationId, pageable);
        List<JournalEntryResponse> data = result.map(this.mapper).stream().toList();
        return new PaginationResponse<>(data, result.getTotalPages(), result.getTotalElements());
    }
}
