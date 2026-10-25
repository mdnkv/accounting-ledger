package dev.mednikov.accounting.ledger.periods.controllers;

import dev.mednikov.accounting.ledger.currencies.domain.CurrencyResponse;
import dev.mednikov.accounting.ledger.periods.domain.CreatePeriodRequest;
import dev.mednikov.accounting.ledger.periods.domain.PeriodResponse;
import dev.mednikov.accounting.ledger.periods.services.PeriodService;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;
import dev.mednikov.accounting.ledger.shared.domain.SortingOrder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/periods")
public class PeriodRestController {

    private final PeriodService periodService;

    public PeriodRestController(PeriodService periodService) {
        this.periodService = periodService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('periods:create') and hasAuthority(#body.organizationId)")
    public @ResponseBody PeriodResponse createPeriod (@RequestBody @Valid CreatePeriodRequest body){
        return this.periodService.createPeriod(body);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('periods:view')")
    public PaginationResponse<PeriodResponse> getPeriods (
            @RequestParam(required = true) UUID organizationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "periodStart-asc") String order,
            @RequestParam(defaultValue = "10") int count
    ){
        SortingOrder sortingOrder = new SortingOrder(order);
        PaginationRequest request = new PaginationRequest(sortingOrder, page, count);
        return this.periodService.getPeriods(organizationId, request);
    }


}
