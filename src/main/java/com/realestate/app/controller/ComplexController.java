package com.realestate.app.controller;

import com.realestate.app.dto.ComplexDTO;
import com.realestate.app.service.ComplexService;
import com.realestate.app.sort.ComplexSortBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/complexes")
public class ComplexController {

    private static final Logger LOG = LoggerFactory.getLogger(ComplexController.class);
    private final ComplexService complexService;

    @Autowired
    public ComplexController(ComplexService complexService) {
        this.complexService = complexService;
    }

    @GetMapping
    public List<ComplexDTO> index(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "CREATED_AT") ComplexSortBy sortProperty,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        return complexService.getAllComplexes(page, size, sortProperty, sortDirection);
    }

    @GetMapping("/{id}")
    public ComplexDTO show(@PathVariable long id) {
        return complexService.getComplexById(id);
    }
}
