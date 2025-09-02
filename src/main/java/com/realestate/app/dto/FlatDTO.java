package com.realestate.app.dto;

import java.time.LocalDateTime;

public record FlatDTO(
        long id,
        long complexId,
        String building,
        String number,
        int floor,
        int rooms,
        double areaTotal,
        int actualPriceTotal,
        int actualPricePerM2,
        LocalDateTime createdAt
) {
}