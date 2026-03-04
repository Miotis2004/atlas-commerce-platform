package com.atlas.notification.service;

import com.atlas.notification.entity.NotificationRecord;
import com.atlas.notification.repository.NotificationRepository;
import java.time.Instant;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createMockNotifications(String eventType, String orderId, String payload) {
        save("EMAIL", eventType, orderId, payload);
        save("SMS", eventType, orderId, payload);
        save("AUDIT", eventType, orderId, payload);
    }

    private void save(String channel, String eventType, String orderId, String payload) {
        NotificationRecord record = new NotificationRecord();
        record.setChannel(channel);
        record.setEventType(eventType);
        record.setOrderId(orderId);
        record.setPayload(payload);
        record.setCreatedAt(Instant.now());
        notificationRepository.save(record);
    }
}
