package dev.mednikov.accounting.ledger.currencies.services;

import dev.mednikov.accounting.ledger.currencies.domain.CreateCurrencyRequest;
import dev.mednikov.accounting.ledger.currencies.domain.CurrencyResponse;
import dev.mednikov.accounting.ledger.currencies.domain.UpdateCurrencyRequest;
import dev.mednikov.accounting.ledger.currencies.exceptions.CurrencyAlreadyExistsException;
import dev.mednikov.accounting.ledger.currencies.exceptions.CurrencyDoesNotExistException;
import dev.mednikov.accounting.ledger.currencies.mappers.CurrencyResponseMapper;
import dev.mednikov.accounting.ledger.currencies.models.Currency;
import dev.mednikov.accounting.ledger.currencies.repositories.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class CurrencyServiceImpl implements CurrencyService{

    private final CurrencyRepository currencyRepository;
    private final CurrencyResponseMapper mapper;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository, CurrencyResponseMapper mapper) {
        this.currencyRepository = currencyRepository;
        this.mapper = mapper;
    }

    @Override
    public CurrencyResponse createCurrency(CreateCurrencyRequest request) {
        String currencyCode = request.code();

        // TODO check that the currency code is valid

        // Check that the code does not exist yet
        if (this.currencyRepository.existsByCurrencyCodeAndOrganizationId(currencyCode, request.organizationId())) {
            throw new CurrencyAlreadyExistsException(request.organizationId(), currencyCode);
        }

        // Check if primary currency already exists
        boolean isPrimary = this.currencyRepository.getPrimaryCurrency(request.organizationId()).isEmpty();

        // Create currency
        Currency currency = new Currency();
        currency.setCurrencyCode(currencyCode);
        currency.setName(request.name());
        currency.setOrganizationId(request.organizationId());
        currency.setActive(true);
        currency.setPrimary(isPrimary);

        // Persist
        Currency result = this.currencyRepository.save(currency);

        // Return dto
        return this.mapper.apply(result);
    }

    @Override
    public CurrencyResponse updateCurrency(UpdateCurrencyRequest request) {
        // Find currency or throw exception
        Currency currency = this.currencyRepository.findById(request.id())
                .orElseThrow(() -> new CurrencyDoesNotExistException(request.id()));

        // Check that if the code is updated, it is not used
        if (!currency.getCurrencyCode().equals(request.code())) {
            String newCurrencyCode = request.code();
            if (this.currencyRepository.existsByCurrencyCodeAndOrganizationId(newCurrencyCode, request.organizationId())) {
                throw new CurrencyAlreadyExistsException(request.organizationId(), newCurrencyCode);
            }
            // Set code otherwise
            currency.setCurrencyCode(newCurrencyCode);
        }

        // Set primary currency
        if (!currency.isPrimary() && request.primary()) {
            // Update current primary currency
            Optional<Currency> currentPrimary = this.currencyRepository.getPrimaryCurrency(request.organizationId());
            if (currentPrimary.isPresent()) {
                Currency cp = currentPrimary.get();
                if (!cp.getId().equals(request.id())) {
                    cp.setPrimary(false);
                    this.currencyRepository.save(cp);
                }
            }

            // update currency to primary
            currency.setPrimary(true);
        }

        // Change other attributes
        currency.setName(request.name());
        currency.setActive(request.active());

        // Persist
        Currency result = this.currencyRepository.save(currency);

        // Return dto
        return this.mapper.apply(result);
    }

    @Override
    public Optional<CurrencyResponse> getCurrencyById(UUID id) {
        return this.currencyRepository.findById(id).map(this.mapper);
    }
}
