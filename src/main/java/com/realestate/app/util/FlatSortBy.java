package com.realestate.app.util;

import org.springframework.data.domain.Sort;

public enum FlatSortBy {
    COMPLEX_ID("complexId"),
    BUILDING("building"),
    CREATED_AT("createdAt");

    private final String property;

    FlatSortBy(String property) {
        this.property = property;
    }

    public Sort getSort(Sort.Direction direction) {
        return Sort.by(direction, property);
    }
}
