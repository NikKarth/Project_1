package com.example.tradesimulator.notification;
import com.example.tradesimulator.model.Order;
public class SMSNotificationDecorator extends NotificationDecorator {

    public SMSNotificationDecorator(NotificationService wrapped) {
        super(wrapped);
    }

    @Override
    public void notifyOrderExecuted(Order order, String message) {
        super.notifyOrderExecuted(order, message);
        // Mock SMS: log a short text message
        System.out.println("SMS: " + message.substring(0, Math.min(160, message.length())));
    }
}