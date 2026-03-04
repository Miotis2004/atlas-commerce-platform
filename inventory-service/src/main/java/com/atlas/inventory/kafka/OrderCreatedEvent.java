package com.atlas.inventory.kafka;

import java.util.List;

public record OrderCreatedEvent(String orderId, List<OrderItem> items) {

    public record OrderItem(Long productId, int quantity) {
    }
}
