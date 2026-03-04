package com.atlas.notification.kafka;

import com.atlas.notification.service.NotificationService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Pattern ORDER_ID_PATTERN = Pattern.compile("\\\"orderId\\\"\\s*:\\s*\\\"([^\\\"]+)\\\"");

    private final NotificationService notificationService;

    public OrderEventConsumer(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @KafkaListener(topics = {"atlas.order.created", "atlas.order.completed", "atlas.order.cancelled"}, groupId = "notification-service")
    public void consume(String payload) {
        String orderId = extractOrderId(payload);
        String eventType = extractEventType(payload);
        notificationService.createMockNotifications(eventType, orderId, payload);
    }

    private String extractOrderId(String payload) {
        if (payload == null) {
            return "unknown-order";
        }
        Matcher matcher = ORDER_ID_PATTERN.matcher(payload);
        return matcher.find() ? matcher.group(1) : "unknown-order";
    }

    private String extractEventType(String payload) {
        if (payload == null) {
            return "UNKNOWN";
        }
        if (payload.contains("reasons")) {
            return "ORDER_CANCELLED";
        }
        if (payload.contains("totalAmount")) {
            return "ORDER_CREATED";
        }
        return "ORDER_COMPLETED";
    }
}
