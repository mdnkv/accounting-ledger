package dev.mednikov.accounting.ledger.shared.domain;

public record PaginationRequest(SortingOrder sortingOrder, int page, int count) {

}
