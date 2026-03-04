package com.atlas.notification.service;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.atlas.notification.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void shouldCreateThreeMockNotifications() {
        notificationService.createMockNotifications("ORDER_CREATED", "order-1", "{}");
        verify(notificationRepository, times(3)).save(org.mockito.ArgumentMatchers.any());
    }
}
