package com.realestate.app.service;

import com.realestate.app.dto.FlatCreateDTO;
import com.realestate.app.dto.FlatDTO;
import com.realestate.app.exception.ResourceNotFoundException;
import com.realestate.app.mapper.FlatMapper;
import com.realestate.app.model.FlatEntity;
import com.realestate.app.repository.ComplexRepository;
import com.realestate.app.repository.FlatRepository;
import com.realestate.app.util.FlatSortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlatService {

    @Autowired
    private FlatRepository flatRepository;

    private ComplexRepository complexRepository;

    public List<FlatDTO> getAllFlats(
            @Nullable Long complexId,
            int page,
            int size,
            FlatSortBy sortProperty,
            Sort.Direction sortDirection
    ) {
        Sort sort = sortProperty.getSort(sortDirection);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        if (complexId == null) {
            return flatRepository.findAll(pageRequest).stream()
                    .map(FlatMapper::toDTO).toList();
        }

        return flatRepository.findAllByComplexId(complexId, pageRequest).stream()
                .map(FlatMapper::toDTO).toList();
    }

    public FlatDTO getFlat(long id) {
        FlatEntity flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat", id));

        return FlatMapper.toDTO(flat);
    }

    public FlatDTO create(FlatCreateDTO dto) {
        FlatEntity flat = FlatMapper.toEntity(dto);

        flatRepository.save(flat);

        return FlatMapper.toDTO(flat);
    }

    public void delete(long flatId) {
        flatRepository.deleteById(flatId);
    }
}
