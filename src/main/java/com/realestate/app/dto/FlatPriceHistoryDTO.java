package com.realestate.app.dto;

import java.time.LocalDateTime;

public record FlatPriceHistoryDTO(
        long id,
        long flatId,
        int priceTotal,
        int pricePerM2,
        LocalDateTime createdAt
) {
}
