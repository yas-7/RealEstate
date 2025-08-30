package com.realestate.app.dto;

public record FlatPriceHistoryCreateDTO(
        long flatId,
        int priceTotal,
        int pricePerM2
) {
}
