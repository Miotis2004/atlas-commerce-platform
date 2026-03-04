package com.atlas.inventory.controller;

import com.atlas.inventory.dto.StockReservationRequest;
import com.atlas.inventory.dto.StockReservationResponse;
import com.atlas.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory/stock")
public class StockController {

    private final InventoryService inventoryService;

    public StockController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @PostMapping("/reserve")
    public StockReservationResponse reserve(@Valid @RequestBody StockReservationRequest request) {
        return StockReservationResponse.from(inventoryService.reserveStock(request));
    }

    @PostMapping("/release/{reservationKey}")
    public StockReservationResponse release(@PathVariable String reservationKey) {
        return StockReservationResponse.from(inventoryService.releaseStock(reservationKey));
    }
}
