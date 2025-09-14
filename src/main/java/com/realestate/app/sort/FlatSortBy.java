package com.realestate.app.sort;

import org.springframework.data.domain.Sort;

public enum FlatSortBy {
    COMPLEX_ID("complexId"),
    BUILDING("building"),
    MODIFIED_AT("modifiedAt"),
    ACTUAL_PRICE_TOTAL("actualPriceTotal"),
    ACTUAL_PRICE_PER_M2("actualPricePerM2");

    private final String property;

    FlatSortBy(String property) {
        this.property = property;
    }

    public Sort getSort(Sort.Direction direction) {
        return Sort.by(direction, property);
    }
}
