package com.example.tradesimulator.notification;

import com.example.tradesimulator.model.Order;

public abstract class NotificationDecorator implements NotificationService {

    protected final NotificationService wrapped;

    public NotificationDecorator(NotificationService wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void notifyOrderExecuted(Order order, String message) {
        wrapped.notifyOrderExecuted(order, message);
    }
}