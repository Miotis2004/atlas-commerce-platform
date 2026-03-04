package com.atlas.common.events;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderCreatedEvent(
        BaseEvent metadata,
        UUID orderId,
        UUID customerId,
        List<OrderItem> items,
        BigDecimal totalAmount
) {
    public record OrderItem(UUID productId, int quantity, BigDecimal unitPrice) {
    }
}
