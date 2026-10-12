package dev.mednikov.accounting.ledger.journals.services;

import dev.mednikov.accounting.ledger.currencies.mappers.CurrencyResponseMapper;
import dev.mednikov.accounting.ledger.currencies.mappers.CurrencyResponseMapperImpl;
import dev.mednikov.accounting.ledger.currencies.repositories.CurrencyRepository;
import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryLineRequest;
import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryRequest;
import dev.mednikov.accounting.ledger.journals.exceptions.NotBalancedEntryException;
import dev.mednikov.accounting.ledger.journals.mappers.JournalEntryResponseMapper;
import dev.mednikov.accounting.ledger.journals.mappers.JournalEntryResponseMapperImpl;
import dev.mednikov.accounting.ledger.journals.models.JournalEntryType;
import dev.mednikov.accounting.ledger.journals.repositories.JournalEntryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

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

}
