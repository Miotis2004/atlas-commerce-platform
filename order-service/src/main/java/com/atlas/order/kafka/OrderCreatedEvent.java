package com.atlas.order.kafka;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreatedEvent(String orderId, String customerId, List<Item> items, BigDecimal totalAmount) {

    public record Item(Long productId, int quantity, BigDecimal unitPrice) {
    }
}
