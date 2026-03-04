package com.atlas.inventory.controller;

import com.atlas.inventory.dto.InventoryItemRequest;
import com.atlas.inventory.dto.InventoryItemResponse;
import com.atlas.inventory.service.InventoryService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory/items")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping
    public List<InventoryItemResponse> findAll() {
        return inventoryService.findAllItems().stream().map(InventoryItemResponse::from).toList();
    }

    @GetMapping("/{id}")
    public InventoryItemResponse findById(@PathVariable Long id) {
        return InventoryItemResponse.from(inventoryService.findItemById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InventoryItemResponse upsert(@Valid @RequestBody InventoryItemRequest request) {
        return InventoryItemResponse.from(inventoryService.upsertItem(request));
    }
}
