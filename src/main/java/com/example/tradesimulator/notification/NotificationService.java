package com.example.tradesimulator.notification;

import com.example.tradesimulator.model.Order;

public interface NotificationService {
    /** Notify the user that an order has executed */
    void notifyOrderExecuted(Order order, String message);
}