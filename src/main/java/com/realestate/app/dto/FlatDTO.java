package com.realestate.app.dto;

import java.time.LocalDate;

public record FlatDTO(
        long id,
        long complexId,
        String building,
        String number,
        int floor,
        int rooms,
        double areaTotal,
        LocalDate createdAt
) {
}