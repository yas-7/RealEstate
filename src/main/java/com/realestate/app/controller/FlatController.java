package com.realestate.app.controller;

import com.realestate.app.dto.FlatCreateDTO;
import com.realestate.app.dto.FlatDTO;
import com.realestate.app.service.FlatService;
import com.realestate.app.util.FlatSortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/flats")
public class FlatController {

    @Autowired
    private FlatService flatService;

    @GetMapping
    public List<FlatDTO> index(
            @RequestParam(required = false) Long complexId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "CREATED_AT") FlatSortBy sortProperty,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection
    ) {
        return flatService.getAllFlats(complexId, page, size, sortProperty, sortDirection);
    }

    @GetMapping("/{flatId}")
    public FlatDTO show(@PathVariable long flatId) {
        return flatService.getFlat(flatId);
    }

    @PostMapping
    public FlatDTO create(@RequestBody FlatCreateDTO dto) {
        return flatService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void destroy(@PathVariable long id) {
        flatService.delete(id);
    }
}
