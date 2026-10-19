package dev.mednikov.accounting.ledger.shared.domain;

import org.springframework.data.domain.Sort;

public final class SortingOrder {

    private final Sort sort;

    public SortingOrder(String sortBy){
        String[] items = sortBy.split("-");
        String fieldName = items[0];
        if (items[1].equalsIgnoreCase("asc")) {
            this.sort = Sort.by(fieldName).ascending();
        } else {
            this.sort = Sort.by(fieldName).descending();
        }
    }

    public Sort getSort() {
        return sort;
    }
}
