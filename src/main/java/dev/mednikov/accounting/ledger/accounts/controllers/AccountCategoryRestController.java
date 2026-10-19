package dev.mednikov.accounting.ledger.accounts.controllers;

import dev.mednikov.accounting.ledger.accounts.domain.AccountCategoryResponse;
import dev.mednikov.accounting.ledger.accounts.domain.CreateAccountCategoryRequest;
import dev.mednikov.accounting.ledger.accounts.domain.UpdateAccountCategoryRequest;
import dev.mednikov.accounting.ledger.accounts.services.AccountCategoryService;
import dev.mednikov.accounting.ledger.shared.domain.PaginationRequest;
import dev.mednikov.accounting.ledger.shared.domain.PaginationResponse;
import dev.mednikov.accounting.ledger.shared.domain.SortingOrder;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('accounts:create') and hasAuthority(#body.organizationId)")
    public @ResponseBody AccountCategoryResponse createAccountCategory (@RequestBody @Valid CreateAccountCategoryRequest body){
        return this.accountCategoryService.createAccountCategory(body);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('accounts:update') and hasAuthority(#body.organizationId)")
    public @ResponseBody AccountCategoryResponse updateAccountCategory (@RequestBody @Valid UpdateAccountCategoryRequest body){
        return this.accountCategoryService.updateAccountCategory(body);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('accounts:view')")
    public PaginationResponse<AccountCategoryResponse> getAccountCategories (
            @RequestParam(required = true) UUID organizationId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "name-asc") String order,
            @RequestParam(defaultValue = "10") int count
    ){
        SortingOrder sortingOrder = new SortingOrder(order);
        PaginationRequest request = new PaginationRequest(sortingOrder, page, count);
        return this.accountCategoryService.getAccountCategories(organizationId, request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('accounts:view')")
    public ResponseEntity<AccountCategoryResponse> getAccountCategoryById (@PathVariable UUID id){
        Optional<AccountCategoryResponse> result = this.accountCategoryService.getAccountCategoryById(id);
        return ResponseEntity.of(result);
    }

}
