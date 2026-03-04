package com.atlas.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record InventoryItemRequest(
        @NotNull Long productId,
        @Min(0) int availableQuantity
) {
}
