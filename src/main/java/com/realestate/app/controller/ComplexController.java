package com.realestate.app.controller;

import com.realestate.app.dto.ComplexCreateDTO;
import com.realestate.app.dto.ComplexDTO;
import com.realestate.app.service.ComplexService;
import com.realestate.app.util.ComplexSortBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComplexDTO create(@RequestBody ComplexCreateDTO dto) {
        return complexService.createComplex(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable long id) {
        complexService.deleteComplex(id);
    }
}
