package com.realestate.app.sort;

import org.springframework.data.domain.Sort;

public enum ComplexSortBy {
    DEVELOPER("developer"),
    NAME("name"),
    CREATED_AT("createdAt");

    private final String property;

    ComplexSortBy(String property) {
        this.property = property;
    }

    public Sort getSort(Sort.Direction direction) {
        return Sort.by(direction, property);
    }
}
