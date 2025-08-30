package com.realestate.app.controller;

import com.realestate.app.dto.ComplexCreateDTO;
import com.realestate.app.dto.ComplexDTO;
import com.realestate.app.service.ComplexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/complexes")
public class ComplexController {

    @Autowired
    private ComplexService complexService;

    @GetMapping
    public List<ComplexDTO> index(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortProperty,
            @RequestParam(defaultValue = "asc") String sortDirection
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

//    @PutMapping("/{id}")
//    public ComplexDTO update(@RequestBody ComplexUpdateDTO, @PathVariable long id) {
//        return complexService.updateComplex(dto, id);
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable long id) {
        complexService.deleteComplex(id);
    }
}
