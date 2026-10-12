package dev.mednikov.accounting.ledger.currencies.services;

import dev.mednikov.accounting.ledger.currencies.domain.CreateCurrencyRequest;
import dev.mednikov.accounting.ledger.currencies.domain.CurrencyResponse;
import dev.mednikov.accounting.ledger.currencies.domain.UpdateCurrencyRequest;
import dev.mednikov.accounting.ledger.currencies.exceptions.CurrencyAlreadyExistsException;
import dev.mednikov.accounting.ledger.currencies.exceptions.CurrencyDoesNotExistException;
import dev.mednikov.accounting.ledger.currencies.mappers.CurrencyResponseMapper;
import dev.mednikov.accounting.ledger.currencies.mappers.CurrencyResponseMapperImpl;
import dev.mednikov.accounting.ledger.currencies.models.Currency;
import dev.mednikov.accounting.ledger.currencies.repositories.CurrencyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {

    @Mock private CurrencyRepository currencyRepository;
    private CurrencyServiceImpl service;

    @BeforeEach
    void setup(){
        CurrencyResponseMapper mapper = new CurrencyResponseMapperImpl();
        this.service = new CurrencyServiceImpl(currencyRepository, mapper);
    }

    @Test
    void createCurrency_currencyCodeAlreadyExistsTest(){
        String currencyCode = "EUR";
        String currencyName = "Euro";
        UUID organizationId = UUID.randomUUID();

        CreateCurrencyRequest requestDto = new CreateCurrencyRequest(organizationId, currencyName, currencyCode);

        when(currencyRepository.existsByCurrencyCodeAndOrganizationId(currencyCode, organizationId)).thenReturn(true);

        assertThatThrownBy(() -> service.createCurrency(requestDto)).isInstanceOf(CurrencyAlreadyExistsException.class);

    }

    @Test
    void createCurrency_createdTest(){
        String currencyCode = "EUR";
        String currencyName = "Euro";
        UUID currencyId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        CreateCurrencyRequest requestDto = new CreateCurrencyRequest(organizationId, currencyName, currencyCode);

        Currency currency = new Currency();
        currency.setId(currencyId);
        currency.setCurrencyCode(currencyCode);
        currency.setName(currencyName);
        currency.setOrganizationId(organizationId);
        currency.setActive(true);
        currency.setPrimary(true);

        when(currencyRepository.existsByCurrencyCodeAndOrganizationId(currencyCode, organizationId)).thenReturn(false);
        when(currencyRepository.getPrimaryCurrency(organizationId)).thenReturn(Optional.empty());
        when(currencyRepository.save(any(Currency.class))).thenReturn(currency);

        CurrencyResponse result = service.createCurrency(requestDto);
        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", currencyName)
                .hasFieldOrPropertyWithValue("organizationId", organizationId)
                .hasFieldOrPropertyWithValue("code", currencyCode)
                .hasFieldOrPropertyWithValue("active", true)
                .hasFieldOrPropertyWithValue("primary", true)
                .hasFieldOrPropertyWithValue("id", currencyId);

    }

    @Test
    void updateCurrency_currencyDoesNotExistTest(){
        String currencyCode = "EUR";
        String currencyName = "Euro";
        UUID currencyId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        UpdateCurrencyRequest requestDto = new UpdateCurrencyRequest(currencyId, organizationId, currencyName, currencyCode, true, true);

        when(currencyRepository.findById(currencyId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.updateCurrency(requestDto)).isInstanceOf(CurrencyDoesNotExistException.class);
    }

    @Test
    void updateCurrency_currencyCodeAlreadyExistsTest(){
        String currencyCode = "EUR";
        String currencyName = "Euro";
        UUID currencyId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        UpdateCurrencyRequest requestDto = new UpdateCurrencyRequest(currencyId, organizationId, currencyName, currencyCode, true, true);


        Currency currency = new Currency();
        currency.setId(currencyId);
        currency.setCurrencyCode("CHF");
        currency.setName(currencyName);
        currency.setOrganizationId(organizationId);

        when(currencyRepository.findById(currencyId)).thenReturn(Optional.of(currency));
        when(currencyRepository.existsByCurrencyCodeAndOrganizationId(currencyCode, organizationId)).thenReturn(true);

        assertThatThrownBy(() -> service.updateCurrency(requestDto)).isInstanceOf(CurrencyAlreadyExistsException.class);
    }

    @Test
    void updateCurrency_updatedTest(){
        String currencyCode = "EUR";
        String currencyName = "Euro";
        UUID currencyId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        UpdateCurrencyRequest requestDto = new UpdateCurrencyRequest(currencyId, organizationId, currencyName, currencyCode, true, true);


        Currency currency = new Currency();
        currency.setId(currencyId);
        currency.setCurrencyCode("CHF");
        currency.setName(currencyName);
        currency.setOrganizationId(organizationId);

        when(currencyRepository.findById(currencyId)).thenReturn(Optional.of(currency));
        when(currencyRepository.existsByCurrencyCodeAndOrganizationId(currencyCode, organizationId)).thenReturn(false);
        when(currencyRepository.save(any(Currency.class))).thenReturn(currency);

        CurrencyResponse result = service.updateCurrency(requestDto);
        assertThat(result).isNotNull();
    }

    @Test
    void getCurrencyById_existsTest(){
        String currencyCode = "EUR";
        String currencyName = "Euro";
        UUID currencyId = UUID.randomUUID();
        UUID organizationId = UUID.randomUUID();

        Currency currency = new Currency();
        currency.setId(currencyId);
        currency.setCurrencyCode(currencyCode);
        currency.setName(currencyName);
        currency.setOrganizationId(organizationId);

        when(currencyRepository.findById(currencyId)).thenReturn(Optional.of(currency));

        Optional<CurrencyResponse> result = service.getCurrencyById(currencyId);
        assertThat(result).isPresent();
    }

    @Test
    void getCurrencyById_doesNotExistTest(){
        UUID currencyId = UUID.randomUUID();

        when(currencyRepository.findById(currencyId)).thenReturn(Optional.empty());

        Optional<CurrencyResponse> result = service.getCurrencyById(currencyId);
        assertThat(result).isEmpty();
    }

}
