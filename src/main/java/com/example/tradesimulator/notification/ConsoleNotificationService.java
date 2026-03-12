package com.example.tradesimulator.notification;

import com.example.tradesimulator.model.Order;

public class ConsoleNotificationService implements NotificationService {

    @Override
    public void notifyOrderExecuted(Order order, String message) {
        System.out.println("[Notification] " + message + " for order: " + order);
    }
}