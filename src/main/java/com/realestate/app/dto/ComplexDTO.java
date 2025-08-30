package com.realestate.app.dto;

import jakarta.annotation.Nullable;

import java.time.LocalDate;

public record ComplexDTO(
        long id,
        String name,
        @Nullable String city,
        @Nullable String address,
        @Nullable String developer,
        LocalDate created
) {
}
