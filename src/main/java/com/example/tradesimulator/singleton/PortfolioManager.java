package com.example.tradesimulator.singleton;

import com.example.tradesimulator.model.Portfolio;
import com.example.tradesimulator.notification.ConsoleNotificationService;
import com.example.tradesimulator.notification.EmailNotificationDecorator;
import com.example.tradesimulator.notification.NotificationService;
import com.example.tradesimulator.service.MarketService;
import com.example.tradesimulator.service.OrderService;
import com.example.tradesimulator.strategy.PriceUpdateStrategy;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PortfolioManager {

    private static PortfolioManager instance;

    private final Map<String, Portfolio> portfolios;
    private final Map<String, OrderService> orderServices;
    private final Map<String, NotificationService> notificationServices;
    private final MarketService marketService;

    /** Private constructor to enforce singleton */
    private PortfolioManager() {
        this.portfolios = new HashMap<>();
        this.orderServices = new HashMap<>();
        this.notificationServices = new HashMap<>();

        // Use RandomWalkStrategy with seeded Random for determinism
        PriceUpdateStrategy strategy = new com.example.tradesimulator.strategy.RandomWalkStrategy(new Random(), 0.02);
        this.marketService = new MarketService(strategy);

        // Initialize 3 users
        initializeUser("user1");
        initializeUser("user2");
        initializeUser("user3");
    }

    private void initializeUser(String userId) {
        // Start user with $10,000
        Portfolio portfolio = new Portfolio(BigDecimal.valueOf(10_000));
        portfolios.put(userId, portfolio);

        // Default notification: console only
        NotificationService notificationService = new ConsoleNotificationService();
        notificationServices.put(userId, notificationService);

        // Initialize OrderService
        OrderService orderService = new OrderService(portfolio, marketService, notificationService);
        orderServices.put(userId, orderService);
    }

    /** Thread-safe singleton accessor */
    public static synchronized PortfolioManager getInstance() {
        if (instance == null) {
            instance = new PortfolioManager();
        }
        return instance;
    }

    public Portfolio getPortfolio(String userId) {
        return portfolios.get(userId);
    }

    public OrderService getOrderService(String userId) {
        return orderServices.get(userId);
    }

    public MarketService getMarketService() {
        return marketService;
    }

    public void setNotificationForUser(String userId, NotificationService notificationService) {
        notificationServices.put(userId, notificationService);
        // Update the orderService with new notification
        OrderService orderService = orderServices.get(userId);
        if (orderService != null) {
            // Since OrderService has final notificationService, recreate
            Portfolio portfolio = portfolios.get(userId);
            OrderService newOrderService = new OrderService(portfolio, marketService, notificationService);
            orderServices.put(userId, newOrderService);
        }
    }

    public Map<String, OrderService> getAllOrderServices() {
        return orderServices;
    }
}