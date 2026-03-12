package com.example.tradesimulator.notification;

import com.example.tradesimulator.model.Order;

public class EmailNotificationDecorator extends NotificationDecorator {

    public EmailNotificationDecorator(NotificationService wrapped) {
        super(wrapped);
    }

    @Override
    public void notifyOrderExecuted(Order order, String message) {
        super.notifyOrderExecuted(order, message);
        // Simulate sending email
        System.out.println("[Email] Sending email: " + message + " for order: " + order);
    }
}