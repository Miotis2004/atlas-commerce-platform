package com.atlas.inventory.dto;

import com.atlas.inventory.entity.InventoryItem;

public record InventoryItemResponse(Long id, Long productId, int availableQuantity, int reservedQuantity) {

    public static InventoryItemResponse from(InventoryItem item) {
        return new InventoryItemResponse(item.getId(), item.getProductId(), item.getAvailableQuantity(), item.getReservedQuantity());
    }
}
