package dev.mednikov.accounting.ledger.accounts.controllers;

import dev.mednikov.accounting.ledger.accounts.domain.AccountCategoryResponse;
import dev.mednikov.accounting.ledger.accounts.domain.CreateAccountCategoryRequest;
import dev.mednikov.accounting.ledger.accounts.domain.UpdateAccountCategoryRequest;
import dev.mednikov.accounting.ledger.accounts.services.AccountCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/account-categories")
public class AccountCategoryRestController {

    private final AccountCategoryService accountCategoryService;

    public AccountCategoryRestController(AccountCategoryService accountCategoryService) {
        this.accountCategoryService = accountCategoryService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody AccountCategoryResponse createAccountCategory (@RequestBody @Valid CreateAccountCategoryRequest body){
        return this.accountCategoryService.createAccountCategory(body);
    }

    @PutMapping
    public @ResponseBody AccountCategoryResponse updateAccountCategory (@RequestBody @Valid UpdateAccountCategoryRequest body){
        return this.accountCategoryService.updateAccountCategory(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountCategoryResponse> getAccountCategoryById (@PathVariable UUID id){
        Optional<AccountCategoryResponse> result = this.accountCategoryService.getAccountCategoryById(id);
        return ResponseEntity.of(result);
    }

}
