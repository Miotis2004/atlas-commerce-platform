package com.atlas.order.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishCreated(OrderCreatedEvent event) {
        kafkaTemplate.send("atlas.order.created", event.orderId(), event);
    }

    public void publishCompleted(OrderCompletedEvent event) {
        kafkaTemplate.send("atlas.order.completed", event.orderId(), event);
    }

    public void publishCancelled(OrderCancelledEvent event) {
        kafkaTemplate.send("atlas.order.cancelled", event.orderId(), event);
    }
}
