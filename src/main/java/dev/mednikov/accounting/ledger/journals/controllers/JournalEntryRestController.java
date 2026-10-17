package dev.mednikov.accounting.ledger.journals.controllers;

import dev.mednikov.accounting.ledger.journals.domain.CreateJournalEntryRequest;
import dev.mednikov.accounting.ledger.journals.domain.JournalEntryResponse;
import dev.mednikov.accounting.ledger.journals.services.JournalEntryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

}
