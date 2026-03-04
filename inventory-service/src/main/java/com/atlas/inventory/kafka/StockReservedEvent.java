package com.atlas.inventory.kafka;

import java.util.List;

public record StockReservedEvent(String orderId, List<Item> reservedItems) {

    public record Item(Long productId, int reservedQuantity) {
    }
}
