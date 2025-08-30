package com.realestate.app.mapper;

import com.realestate.app.dto.FlatPriceHistoryCreateDTO;
import com.realestate.app.dto.FlatPriceHistoryDTO;
import com.realestate.app.model.FlatPriceHistoryEntity;

public class FlatPriceHistoryMapper {
    public static FlatPriceHistoryDTO toDTO(FlatPriceHistoryEntity flatPriceHistory) {
        return new FlatPriceHistoryDTO(
                flatPriceHistory.getId(),
                flatPriceHistory.getFlatId(),
                flatPriceHistory.getPriceTotal(),
                flatPriceHistory.getPricePerM2(),
                flatPriceHistory.getCreatedAt()
        );
    }

    public static FlatPriceHistoryEntity toEntity(FlatPriceHistoryCreateDTO dto) {
        FlatPriceHistoryEntity flatPriceHistory = new FlatPriceHistoryEntity();
        flatPriceHistory.setFlatId(dto.flatId());
        flatPriceHistory.setPriceTotal(dto.priceTotal());
        flatPriceHistory.setPricePerM2(dto.pricePerM2());

        return flatPriceHistory;
    }
}
