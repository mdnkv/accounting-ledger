package dev.mednikov.accounting.ledger.currencies.controllers;

import java.util.Optional;
import java.util.UUID;

import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;
import dev.mednikov.accounting.ledger.shared.domain.SortingOrder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import dev.mednikov.accounting.ledger.currencies.domain.CreateCurrencyRequest;
import dev.mednikov.accounting.ledger.currencies.domain.CurrencyResponse;
import dev.mednikov.accounting.ledger.currencies.domain.UpdateCurrencyRequest;
import dev.mednikov.accounting.ledger.currencies.services.CurrencyService;

@RestController
@RequestMapping("/currencies")
public class CurrencyRestController {

    private final CurrencyService currencyService;

    public CurrencyRestController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('currencies:create') and hasAuthority(#body.organizationId)")
    public @ResponseBody CurrencyResponse createCurrency (@RequestBody @Valid CreateCurrencyRequest body){
        return this.currencyService.createCurrency(body);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('currencies:update') and hasAuthority(#body.organizationId)")
    public @ResponseBody CurrencyResponse updateCurrency (@RequestBody @Valid UpdateCurrencyRequest body){
        return this.currencyService.updateCurrency(body);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('currencies:view')")
    public PaginationResponse<CurrencyResponse> getCurrencies (
            @RequestParam(required = true) UUID organizationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "name-asc") String order,
            @RequestParam(defaultValue = "10") int count
    ){
        SortingOrder sortingOrder = new SortingOrder(order);
        PaginationRequest request = new PaginationRequest(sortingOrder, page, count);
        return this.currencyService.getCurrencies(organizationId, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('currencies:view')")
    public ResponseEntity<CurrencyResponse> getCurrencyById (@PathVariable UUID id){
        Optional<CurrencyResponse> result = this.currencyService.getCurrencyById(id);
        return ResponseEntity.of(result);
    }

}
