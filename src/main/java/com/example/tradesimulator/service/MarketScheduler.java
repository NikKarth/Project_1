package com.example.tradesimulator.service;

import com.example.tradesimulator.singleton.PortfolioManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MarketScheduler {

    private final PortfolioManager portfolioManager;

    public MarketScheduler() {
        this.portfolioManager = PortfolioManager.getInstance();
    }

    /** Update market prices every 5 seconds */
    @Scheduled(fixedRate = 5000)
    public void updateMarket() {
        portfolioManager.getMarketService().updateMarket();
        // Check limit orders for all users
        for (OrderService orderService : portfolioManager.getAllOrderServices().values()) {
            orderService.checkLimitOrders();
        }
    }
}