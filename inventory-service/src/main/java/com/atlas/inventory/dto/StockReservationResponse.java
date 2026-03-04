package com.atlas.inventory.dto;

import com.atlas.inventory.entity.ReservationStatus;
import com.atlas.inventory.entity.StockReservation;

public record StockReservationResponse(
        Long id,
        String reservationKey,
        String orderId,
        Long productId,
        int quantity,
        ReservationStatus status
) {

    public static StockReservationResponse from(StockReservation reservation) {
        return new StockReservationResponse(
                reservation.getId(),
                reservation.getReservationKey(),
                reservation.getOrderId(),
                reservation.getProductId(),
                reservation.getQuantity(),
                reservation.getStatus());
    }
}
