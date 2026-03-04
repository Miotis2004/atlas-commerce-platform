package com.atlas.order.kafka;

public record StockRejectedEvent(String orderId, Long productId, int requestedQuantity, String reason) {
}
