package com.atlas.common.events;

import java.util.List;
import java.util.UUID;

public record StockReservedEvent(
        BaseEvent metadata,
        UUID orderId,
        List<ReservedItem> reservedItems
) {
    public record ReservedItem(UUID productId, int reservedQuantity) {
    }
}
