package dev.mednikov.accounting.ledger.accounts.controllers;

import dev.mednikov.accounting.ledger.accounts.domain.AccountResponse;
import dev.mednikov.accounting.ledger.accounts.domain.CreateAccountRequest;
import dev.mednikov.accounting.ledger.accounts.domain.UpdateAccountRequest;
import dev.mednikov.accounting.ledger.accounts.services.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/accounts")
public class AccountRestController {

    private final AccountService accountService;

    public AccountRestController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody AccountResponse createAccount (@RequestBody @Valid CreateAccountRequest body){
        return this.accountService.createAccount(body);
    }

    @PutMapping
    public @ResponseBody AccountResponse updateAccount (@RequestBody @Valid UpdateAccountRequest body){
        return this.accountService.updateAccount(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById (@PathVariable UUID id){
        Optional<AccountResponse> result = this.accountService.getAccountById(id);
        return ResponseEntity.of(result);
    }

}
