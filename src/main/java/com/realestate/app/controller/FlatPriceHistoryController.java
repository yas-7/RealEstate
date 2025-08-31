package com.realestate.app.controller;

import com.realestate.app.dto.FlatPriceHistoryCreateDTO;
import com.realestate.app.dto.FlatPriceHistoryDTO;
import com.realestate.app.service.FlatPriceHistoryService;
import com.realestate.app.util.FlatPriceHistorySortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/history")
public class FlatPriceHistoryController {

    @Autowired
    private FlatPriceHistoryService flatPriceHistoryService;

    @GetMapping
    public List<FlatPriceHistoryDTO> index(
            @RequestParam Long flatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "CREATED_AT") FlatPriceHistorySortBy sortProperty,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection
    ) {
        return flatPriceHistoryService.getFlatPriceHistoryByFlatId(flatId, page, size, sortProperty, sortDirection);
    }

    @PostMapping
    public FlatPriceHistoryDTO create(@RequestBody FlatPriceHistoryCreateDTO dto) {
        return flatPriceHistoryService.createFlatPriceHistory(dto);
    }
}
