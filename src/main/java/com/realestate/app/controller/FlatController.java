package com.realestate.app.controller;

import com.realestate.app.dto.FlatDTO;
import com.realestate.app.dto.FlatPageDTO;
import com.realestate.app.service.FlatService;
import com.realestate.app.util.FlatSortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/flats")
public class FlatController {

    private final FlatService flatService;

    @Autowired
    public FlatController(FlatService flatService) {
        this.flatService = flatService;
    }

    @GetMapping
    public FlatPageDTO index(
            @RequestParam(required = false) Long complexId,
            @RequestParam(required = false) String building,
            @RequestParam(required = false) String flatNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "MODIFIED_AT") FlatSortBy sortProperty,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        return flatService.getAllFlats(complexId, building, flatNumber, page, size, sortProperty, sortDirection);
    }

    @GetMapping("/{flatId}")
    public FlatDTO show(@PathVariable long flatId) {
        return flatService.getFlat(flatId);
    }
}
