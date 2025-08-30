package com.realestate.app.mapper;

import com.realestate.app.dto.ComplexCreateDTO;
import com.realestate.app.dto.ComplexDTO;
import com.realestate.app.model.ComplexEntity;

public class ComplexMapper {

    public static ComplexDTO toDTO(ComplexEntity complex) {
        ComplexDTO dto = new ComplexDTO(complex.getId(),
                complex.getName(),
                complex.getCity(),
                complex.getAddress(),
                complex.getDeveloper(),
                complex.getCreatedAt());
        return dto;
    }

    public static ComplexEntity toEntity(ComplexCreateDTO dto) {
        ComplexEntity complex = new ComplexEntity();
        complex.setName(dto.name());
        complex.setCity(dto.city());
        complex.setAddress(dto.address());
        complex.setDeveloper(dto.developer());

        return complex;
    }

}
