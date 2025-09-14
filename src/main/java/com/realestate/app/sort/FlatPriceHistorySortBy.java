package com.realestate.app.sort;

import org.springframework.data.domain.Sort;

public enum FlatPriceHistorySortBy {
    FLAT_ID("flatId"),
    PRICE_TOTAL("priceTotal"),
    PRICE_PER_M2("pricePerM2"),
    CREATED_AT("createdAt");

    private final String property;

    FlatPriceHistorySortBy(String property) {
        this.property = property;
    }

   public Sort getSort(Sort.Direction direction) {
       return Sort.by(direction, property);
   }
}
