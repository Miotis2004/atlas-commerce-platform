package com.atlas.notification.repository;

import com.atlas.notification.entity.NotificationRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationRecord, String> {
}
