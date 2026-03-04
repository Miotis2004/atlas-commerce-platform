package com.atlas.order.dto;

import com.atlas.order.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record OrderResponse(
        String orderId,
        String customerId,
        OrderStatus status,
        BigDecimal totalAmount,
        Instant createdAt,
        List<Item> items
) {
    public record Item(Long productId, int quantity, BigDecimal unitPrice) {
    }
}
