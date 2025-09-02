package com.realestate.app.service;

import com.realestate.app.dto.FlatPriceHistoryCreateDTO;
import com.realestate.app.dto.FlatPriceHistoryDTO;
import com.realestate.app.mapper.FlatPriceHistoryMapper;
import com.realestate.app.model.FlatPriceHistoryEntity;
import com.realestate.app.repository.FlatPriceHistoryRepository;
import com.realestate.app.util.FlatPriceHistorySortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlatPriceHistoryService {

    private final FlatPriceHistoryRepository flatPriceHistoryRepository;

    @Autowired
    public FlatPriceHistoryService(FlatPriceHistoryRepository flatPriceHistoryRepository) {
        this.flatPriceHistoryRepository = flatPriceHistoryRepository;
    }

    public List<FlatPriceHistoryDTO> getFlatPriceHistoryByFlatId(
            Long id,
            int page,
            int size,
            FlatPriceHistorySortBy sortProperty,
            Sort.Direction sortDirection
    ) {
        Sort sort = sortProperty.getSort(sortDirection);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return flatPriceHistoryRepository.findAllByFlatId(id, pageRequest).stream()
                .map(FlatPriceHistoryMapper::toDTO).toList();
    }

    public FlatPriceHistoryDTO createFlatPriceHistory(FlatPriceHistoryCreateDTO dto) {
        FlatPriceHistoryEntity flatPriceHistory = FlatPriceHistoryMapper.toEntity(dto);
        flatPriceHistoryRepository.save(flatPriceHistory);

        return FlatPriceHistoryMapper.toDTO(flatPriceHistory);
    }
}
