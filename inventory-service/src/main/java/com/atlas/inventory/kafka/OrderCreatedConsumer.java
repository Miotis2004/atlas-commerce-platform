package com.atlas.inventory.kafka;

import com.atlas.inventory.dto.StockReservationRequest;
import com.atlas.inventory.exception.StockOperationException;
import com.atlas.inventory.service.InventoryService;
import java.util.List;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedConsumer {

    private final InventoryService inventoryService;
    private final InventoryEventPublisher inventoryEventPublisher;

    public OrderCreatedConsumer(InventoryService inventoryService, InventoryEventPublisher inventoryEventPublisher) {
        this.inventoryService = inventoryService;
        this.inventoryEventPublisher = inventoryEventPublisher;
    }

    @RetryableTopic(dltStrategy = DltStrategy.ALWAYS_RETRY_ON_ERROR)
    @KafkaListener(topics = "atlas.order.created", groupId = "inventory-service")
    public void onOrderCreated(OrderCreatedEvent event) {
        if (event == null || event.items() == null || event.items().isEmpty()) {
            return;
        }

        for (OrderCreatedEvent.OrderItem item : event.items()) {
            String reservationKey = event.orderId() + ":" + item.productId();
            try {
                inventoryService.reserveStock(new StockReservationRequest(
                        reservationKey,
                        event.orderId(),
                        item.productId(),
                        item.quantity()
                ));
                inventoryEventPublisher.publishReserved(new StockReservedEvent(
                        event.orderId(),
                        List.of(new StockReservedEvent.Item(item.productId(), item.quantity()))
                ));
            } catch (StockOperationException ex) {
                inventoryEventPublisher.publishRejected(new StockRejectedEvent(
                        event.orderId(),
                        item.productId(),
                        item.quantity(),
                        ex.getMessage()
                ));
            }
        }
    }
}
