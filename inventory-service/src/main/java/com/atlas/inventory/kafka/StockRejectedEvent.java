package com.atlas.inventory.kafka;

public record StockRejectedEvent(String orderId, Long productId, int requestedQuantity, String reason) {
}
