package com.example.tradesimulator.notification;

import com.example.tradesimulator.model.Order;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Test
    void testNotificationSent() {
        NotificationService mockService = mock(NotificationService.class);
        Order order = mock(Order.class);
        mockService.notifyOrderExecuted(order, "Test message");

        verify(mockService, times(1)).notifyOrderExecuted(order, "Test message");
    }
}