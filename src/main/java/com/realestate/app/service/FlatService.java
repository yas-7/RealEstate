package com.realestate.app.service;

import com.realestate.app.dto.FlatCreateDTO;
import com.realestate.app.dto.FlatDTO;
import com.realestate.app.exception.ResourceNotFoundException;
import com.realestate.app.mapper.FlatMapper;
import com.realestate.app.model.FlatEntity;
import com.realestate.app.repository.ComplexRepository;
import com.realestate.app.repository.FlatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlatService {

    @Autowired
    private FlatRepository flatRepository;

    private ComplexRepository complexRepository;

    public List<FlatDTO> getAllFlatsByComplexId(Long complexId, int page, int size, String sortProperty, String sortDirection) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC
                : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortProperty);
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);

        if (complexId == null) {
            return flatRepository.findAll().stream()
                    .map(FlatMapper::toDTO).toList();
        }
        return flatRepository.findByComplexId(complexId).stream()
                .map(FlatMapper::toDTO).toList();
    }

    public FlatDTO getFlat(long id) {
        FlatEntity flat = flatRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Flat with id " + id + " not found"));

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
