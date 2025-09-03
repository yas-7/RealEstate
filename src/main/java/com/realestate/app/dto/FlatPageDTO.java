package com.realestate.app.dto;

import java.util.List;

public record FlatPageDTO(
        List<FlatDTO> content,
        int number,
        boolean last
) {
}