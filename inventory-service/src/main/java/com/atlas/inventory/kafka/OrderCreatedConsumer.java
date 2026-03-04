package com.atlas.inventory.kafka;

import com.atlas.inventory.dto.StockReservationRequest;
import com.atlas.inventory.exception.StockOperationException;
import com.atlas.inventory.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderCreatedConsumer.class);
    private final InventoryService inventoryService;

    public OrderCreatedConsumer(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @KafkaListener(topics = "atlas.order.created", groupId = "inventory-service")
    public void onOrderCreated(OrderCreatedEvent event) {
        if (event == null || event.items() == null) {
            return;
        }

        event.items().forEach(item -> {
            String reservationKey = event.orderId() + ":" + item.productId();
            try {
                inventoryService.reserveStock(new StockReservationRequest(
                        reservationKey,
                        event.orderId(),
                        item.productId(),
                        item.quantity()
                ));
            } catch (StockOperationException ex) {
                LOGGER.warn("Stock reservation rejected for order {} product {}: {}",
                        event.orderId(), item.productId(), ex.getMessage());
            }
        });
    }
}
