package com.realestate.app.controller;

import com.realestate.app.dto.FlatCreateDTO;
import com.realestate.app.dto.FlatDTO;
import com.realestate.app.service.FlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/flats")
public class FlatController {

    @Autowired
    private FlatService flatService;

    @GetMapping
    public List<FlatDTO> getByComplexId(
            @RequestParam(required = false) Long complexId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortProperty,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return flatService.getAllFlatsByComplexId(complexId, page, size, sortProperty, sortDirection);
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
