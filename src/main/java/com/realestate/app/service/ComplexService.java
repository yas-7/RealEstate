package com.realestate.app.service;

import com.realestate.app.dto.ComplexCreateDTO;
import com.realestate.app.dto.ComplexDTO;
import com.realestate.app.exception.ResourceNotFoundException;
import com.realestate.app.mapper.ComplexMapper;
import com.realestate.app.model.ComplexEntity;
import com.realestate.app.repository.ComplexRepository;
import com.realestate.app.sort.ComplexSortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplexService {

    private final ComplexRepository complexRepository;

    @Autowired
    public ComplexService(ComplexRepository complexRepository) {
        this.complexRepository = complexRepository;
    }

    public List<ComplexDTO> getAllComplexes(int page, int size, ComplexSortBy sortProperty, Sort.Direction sortDirection) {
        Sort sort = sortProperty.getSort(sortDirection);
        PageRequest pageRequest = PageRequest.of(page, size, sort);

        return complexRepository.findAll(pageRequest).stream()
                .map(ComplexMapper::toDTO).toList();
    }

    public ComplexDTO createComplex(ComplexCreateDTO dto) {
        ComplexEntity complex = ComplexMapper.toEntity(dto);
        complexRepository.save(complex);

        return ComplexMapper.toDTO(complex);
    }

    public ComplexDTO getComplexById(long id) {
        ComplexEntity complex = complexRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Complex", id));

        return ComplexMapper.toDTO(complex);
    }

    public void deleteComplex(long id) {
        complexRepository.deleteById(id);
    }
}
