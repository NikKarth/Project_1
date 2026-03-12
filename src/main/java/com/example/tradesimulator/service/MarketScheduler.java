package com.example.tradesimulator.service;

import com.example.tradesimulator.singleton.PortfolioManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MarketScheduler {

    private final MarketService marketService;
    private final OrderService orderService;

    public MarketScheduler() {
        var manager = PortfolioManager.getInstance();
        this.marketService = manager.getMarketService();

        // Setup OrderService with notifications
        this.orderService = new OrderService(manager.getPortfolio(), marketService,
                new com.example.tradesimulator.notification.ConsoleNotificationService());
    }

    /** Update market prices every 5 seconds */
    @Scheduled(fixedRate = 5000)
    public void updateMarket() {
        marketService.updateMarket();
        orderService.checkLimitOrders();
    }
}