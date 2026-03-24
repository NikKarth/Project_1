package com.example.tradesimulator.notification;

import com.example.tradesimulator.model.Order;

public class DashboardNotificationDecorator extends NotificationDecorator {

    private static int badgeCounter = 0;

    public DashboardNotificationDecorator(NotificationService wrapped) {
        super(wrapped);
    }

    @Override
    public void notifyOrderExecuted(Order order, String message) {
        super.notifyOrderExecuted(order, message);
        // Increment dashboard badge counter
        badgeCounter++;
    }

    public static int getBadgeCounter() {
        return badgeCounter;
    }

    public static void resetBadgeCounter() {
        badgeCounter = 0;
    }
}