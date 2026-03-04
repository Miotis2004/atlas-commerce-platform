package com.atlas.order.kafka;

import java.util.List;

public record OrderCancelledEvent(String orderId, List<String> reasons) {
}
