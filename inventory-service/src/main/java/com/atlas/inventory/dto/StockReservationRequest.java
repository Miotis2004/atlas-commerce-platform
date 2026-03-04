package com.atlas.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StockReservationRequest(
        @NotBlank String reservationKey,
        @NotBlank String orderId,
        @NotNull Long productId,
        @Min(1) int quantity
) {
}
