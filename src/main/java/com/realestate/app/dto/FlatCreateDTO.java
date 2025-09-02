package com.realestate.app.dto;

public record FlatCreateDTO(
        long complexId,
        String building,
        String number,
        int floor,
        int rooms,
        double areaTotal,
        int actualPriceTotal,
        int actualPricePerM2
) {
}
