package dev.mednikov.accounting.ledger.shared.domain;

import java.util.List;

public record PaginationResponse<T>(List<T> data, int totalPages, long totalCount) {

}
