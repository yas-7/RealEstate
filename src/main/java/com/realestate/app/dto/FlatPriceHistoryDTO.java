package com.realestate.app.dto;

import java.time.LocalDate;

public record FlatPriceHistoryDTO(
        long id,
        long flatId,
        int priceTotal,
        int pricePerM2,
        LocalDate createdAt
) {
}
