package com.atlas.order.kafka;

import com.atlas.order.service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class StockEventConsumer {

    private final OrderService orderService;

    public StockEventConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(topics = "atlas.stock.reserved", groupId = "order-service")
    public void onStockReserved(StockReservedEvent event) {
        if (event != null && event.orderId() != null) {
            orderService.markReserved(event.orderId());
        }
    }

    @KafkaListener(topics = "atlas.stock.rejected", groupId = "order-service")
    public void onStockRejected(StockRejectedEvent event) {
        if (event != null && event.orderId() != null) {
            String reason = event.reason() == null ? "stock rejected" : event.reason();
            orderService.markRejected(event.orderId(), reason);
        }
    }
}
