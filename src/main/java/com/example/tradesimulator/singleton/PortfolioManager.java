package com.example.tradesimulator.singleton;

import com.example.tradesimulator.model.Portfolio;
import com.example.tradesimulator.notification.ConsoleNotificationService;
import com.example.tradesimulator.notification.EmailNotificationDecorator;
import com.example.tradesimulator.notification.NotificationService;
import com.example.tradesimulator.service.MarketService;
import com.example.tradesimulator.service.OrderService;
import com.example.tradesimulator.strategy.PriceUpdateStrategy;

import java.math.BigDecimal;
import java.util.Random;

public class PortfolioManager {

    private static PortfolioManager instance;

    private final Portfolio portfolio;
    private final MarketService marketService;
    private final OrderService orderService;

    /** Private constructor to enforce singleton */
    private PortfolioManager() {
        // Start user with $10,000
        this.portfolio = new Portfolio(BigDecimal.valueOf(10_000));

        // Use RandomWalkStrategy with seeded Random for determinism
        PriceUpdateStrategy strategy = new com.example.tradesimulator.strategy.RandomWalkStrategy(new Random(), 0.02);
        this.marketService = new MarketService(strategy);

        // Initialize OrderService with notifications
        NotificationService notificationService = new ConsoleNotificationService();
        notificationService = new EmailNotificationDecorator(notificationService);
        this.orderService = new OrderService(portfolio, marketService, notificationService);
    }

    /** Thread-safe singleton accessor */
    public static synchronized PortfolioManager getInstance() {
        if (instance == null) {
            instance = new PortfolioManager();
        }
        return instance;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public MarketService getMarketService() {
        return marketService;
    }

    public OrderService getOrderService() {
        return orderService;
    }
}