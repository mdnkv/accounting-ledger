package dev.mednikov.accounting.ledger.journals.controllers;

import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryRequest;
import dev.mednikov.accounting.ledger.journals.domain.JournalEntryResponse;
import dev.mednikov.accounting.ledger.journals.services.JournalEntryService;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;
import dev.mednikov.accounting.ledger.shared.domain.SortingOrder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/journals")
public class JournalEntryRestController {

    private final JournalEntryService journalEntryService;

    public JournalEntryRestController(JournalEntryService journalEntryService) {
        this.journalEntryService = journalEntryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('journals:create') and hasAuthority(#body.organizationId)")
    public @ResponseBody JournalEntryResponse createJournalEntry (@RequestBody @Valid CreateJournalEntryRequest body){
        return this.journalEntryService.createJournalEntry(body);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('journals:view')")
    public PaginationResponse<JournalEntryResponse> getJournalEntries (
            @RequestParam(required = true) UUID organizationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "name-asc") String order,
            @RequestParam(defaultValue = "10") int count
    ){
        SortingOrder sortingOrder = new SortingOrder(order);
        PaginationRequest request = new PaginationRequest(sortingOrder, page, count);
        return this.journalEntryService.getJournalEntries(organizationId, request);
    }

}
