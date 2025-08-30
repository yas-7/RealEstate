package com.realestate.app.service;

import com.realestate.app.dto.FlatPriceHistoryCreateDTO;
import com.realestate.app.dto.FlatPriceHistoryDTO;
import com.realestate.app.mapper.FlatPriceHistoryMapper;
import com.realestate.app.model.FlatPriceHistoryEntity;
import com.realestate.app.repository.FlatPriceHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlatPriceHistoryService {

    @Autowired
    private FlatPriceHistoryRepository flatPriceHistoryRepository;

    public List<FlatPriceHistoryDTO> getFlatPriceHistoryByFlatId(Long id, int page, int size, String sortProperty, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortProperty);
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);

        if (id == null) {
            return flatPriceHistoryRepository.findAll(pageRequest).stream()
                    .map(FlatPriceHistoryMapper::toDTO).toList();
        }
        return flatPriceHistoryRepository.findAllByFlatId(id, pageRequest).stream()
                .map(FlatPriceHistoryMapper::toDTO).toList();
    }

    public FlatPriceHistoryDTO createFlatPriceHistory(FlatPriceHistoryCreateDTO dto) {
        FlatPriceHistoryEntity flatPriceHistory = FlatPriceHistoryMapper.toEntity(dto);
        flatPriceHistoryRepository.save(flatPriceHistory);

        return FlatPriceHistoryMapper.toDTO(flatPriceHistory);
    }
}
