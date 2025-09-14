package com.realestate.app.controller;

import com.realestate.app.dto.FlatPriceHistoryDTO;
import com.realestate.app.service.FlatPriceHistoryService;
import com.realestate.app.sort.FlatPriceHistorySortBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/history")
public class FlatPriceHistoryController {

    private final FlatPriceHistoryService flatPriceHistoryService;

    @Autowired
    public FlatPriceHistoryController(FlatPriceHistoryService flatPriceHistoryService) {
        this.flatPriceHistoryService = flatPriceHistoryService;
    }

    @GetMapping
    public List<FlatPriceHistoryDTO> index(
            @RequestParam Long flatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "365") int size,
            @RequestParam(defaultValue = "CREATED_AT") FlatPriceHistorySortBy sortProperty,
            @RequestParam(defaultValue = "DESC") Sort.Direction sortDirection
    ) {
        return flatPriceHistoryService.getFlatPriceHistoryByFlatId(flatId, page, size, sortProperty, sortDirection);
    }
}
