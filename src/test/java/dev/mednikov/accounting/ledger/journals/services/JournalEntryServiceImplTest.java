package dev.mednikov.accounting.ledger.journals.services;

import dev.mednikov.accounting.ledger.currencies.mappers.CurrencyResponseMapper;
import dev.mednikov.accounting.ledger.currencies.mappers.CurrencyResponseMapperImpl;
import dev.mednikov.accounting.ledger.currencies.models.Currency;
import dev.mednikov.accounting.ledger.currencies.repositories.CurrencyRepository;
import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryLineRequest;
import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryRequest;
import dev.mednikov.accounting.ledger.journals.domain.JournalEntryResponse;
import dev.mednikov.accounting.ledger.journals.exceptions.NotBalancedEntryException;
import dev.mednikov.accounting.ledger.journals.mappers.JournalEntryResponseMapper;
import dev.mednikov.accounting.ledger.journals.mappers.JournalEntryResponseMapperImpl;
import dev.mednikov.accounting.ledger.journals.models.JournalEntry;
import dev.mednikov.accounting.ledger.journals.models.JournalEntryType;
import dev.mednikov.accounting.ledger.journals.repositories.JournalEntryRepository;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;
import dev.mednikov.accounting.ledger.shared.domain.SortingOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JournalEntryServiceImplTest {

    @Mock private JournalEntryLineService journalEntryLineService;
    @Mock private CurrencyRepository currencyRepository;
    @Mock private JournalEntryRepository journalEntryRepository;

    private JournalEntryServiceImpl service;

    @BeforeEach
    void setup(){
        CurrencyResponseMapper currencyResponseMapper = new CurrencyResponseMapperImpl();
        JournalEntryResponseMapper journalEntryResponseMapper = new JournalEntryResponseMapperImpl(currencyResponseMapper);
        this.service = new JournalEntryServiceImpl(
                journalEntryLineService,
                journalEntryRepository,
                currencyRepository,
                journalEntryResponseMapper
        );
    }

    @Test
    void createJournalEntry_notBalancedTest(){
        UUID currencyId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();
        CreateJournalEntryRequest request = new CreateJournalEntryRequest(
                organizationId,
                currencyId,
                JournalEntryType.GENERAL,
                "Des",
                LocalDate.now(),
                List.of(
                        new CreateJournalEntryLineRequest(UUID.randomUUID(), BigDecimal.valueOf(500.0), BigDecimal.ZERO, 1),
                        new CreateJournalEntryLineRequest(UUID.randomUUID(), BigDecimal.ZERO, BigDecimal.valueOf(400), 2)
                )
        );

        assertThatThrownBy(() -> service.createJournalEntry(request)).isInstanceOf(NotBalancedEntryException.class);
    }

    @Test
    void getJournalEntriesTest(){
        UUID currencyId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        Currency currency = new Currency();
        currency.setId(currencyId);
        currency.setCurrencyCode("EUR");
        currency.setName("Euro");
        currency.setOrganizationId(organizationId);
        currency.setActive(true);
        currency.setPrimary(true);

        List<JournalEntry> journalEntries = new ArrayList<>();

        JournalEntry journalEntry1 = new JournalEntry();
        journalEntry1.setJournalEntryType(JournalEntryType.GENERAL);
        journalEntry1.setTotalCredit(BigDecimal.valueOf(500));
        journalEntry1.setTotalDebit(BigDecimal.valueOf(500));
        journalEntry1.setCurrency(currency);
        journalEntry1.setOrganizationId(organizationId);
        journalEntry1.setDate(LocalDate.now().minusDays(30));
        journalEntry1.setDescription("In hac habitasse platea dictumst");
        journalEntry1.setId(UUID.randomUUID());
        journalEntries.add(journalEntry1);

        JournalEntry journalEntry2 = new JournalEntry();
        journalEntry2.setJournalEntryType(JournalEntryType.GENERAL);
        journalEntry2.setTotalCredit(BigDecimal.valueOf(1000));
        journalEntry2.setTotalDebit(BigDecimal.valueOf(1000));
        journalEntry2.setCurrency(currency);
        journalEntry2.setOrganizationId(organizationId);
        journalEntry2.setDate(LocalDate.now().minusDays(15));
        journalEntry2.setDescription("In egestas massa sed est dapibus");
        journalEntry2.setId(UUID.randomUUID());
        journalEntries.add(journalEntry2);

        String sortBy = "date-asc";
        Pageable pageable = PageRequest.of(1, 10, Sort.by("date").ascending());
        Page<JournalEntry> page = new PageImpl<>(journalEntries, pageable, 50);

        SortingOrder sortingOrder = new SortingOrder(sortBy);
        PaginationRequest request = new PaginationRequest(sortingOrder, 1, 10);

        when(journalEntryRepository.findAllByOrganizationId(any(), any())).thenReturn(page);

        PaginationResponse<JournalEntryResponse> result = service.getJournalEntries(organizationId, request);
        assertThat(result).isNotNull()
                .hasFieldOrProperty("data")
                .hasFieldOrPropertyWithValue("totalPages", 5)
                .hasFieldOrPropertyWithValue("totalCount", 50L);

        assertThat(result.data()).hasSize(journalEntries.size());
    }

}
