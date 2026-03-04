package com.atlas.inventory.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public InventoryEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishReserved(StockReservedEvent event) {
        kafkaTemplate.send("atlas.stock.reserved", event.orderId(), event);
    }

    public void publishRejected(StockRejectedEvent event) {
        kafkaTemplate.send("atlas.stock.rejected", event.orderId(), event);
    }
}
