package com.realestate.app.mapper;

import com.realestate.app.dto.FlatCreateDTO;
import com.realestate.app.dto.FlatDTO;
import com.realestate.app.model.FlatEntity;

public class FlatMapper {
    public static FlatDTO toDTO(FlatEntity flat) {
        return new FlatDTO(
                flat.getId(),
                flat.getComplexId(),
                flat.getBuilding(),
                flat.getNumber(),
                flat.getFloor(),
                flat.getRooms(),
                flat.getAreaTotal(),
                flat.getActualPriceTotal(),
                flat.getActualPricePerM2(),
                flat.getCreatedAt()
        );
    }

    public static FlatEntity toEntity(FlatCreateDTO dto) {
        FlatEntity flat = new FlatEntity();
        flat.setComplexId(dto.complexId());
        flat.setBuilding(dto.building());
        flat.setNumber(dto.number());
        flat.setFloor(dto.floor());
        flat.setRooms(dto.rooms());
        flat.setAreaTotal(dto.areaTotal());
        flat.setActualPriceTotal(dto.actualPriceTotal());
        flat.setActualPricePerM2(dto.actualPricePerM2());

        return flat;
    }
}
