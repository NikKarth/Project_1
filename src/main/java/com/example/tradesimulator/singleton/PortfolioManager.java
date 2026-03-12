package com.example.tradesimulator.singleton;

import com.example.tradesimulator.model.Portfolio;
import com.example.tradesimulator.service.MarketService;
import com.example.tradesimulator.strategy.PriceUpdateStrategy;

import java.math.BigDecimal;
import java.util.Random;

public class PortfolioManager {

    private static PortfolioManager instance;

    private final Portfolio portfolio;
    private final MarketService marketService;

    /** Private constructor to enforce singleton */
    private PortfolioManager() {
        // Start user with $10,000
        this.portfolio = new Portfolio(BigDecimal.valueOf(10_000));

        // Use RandomWalkStrategy with seeded Random for determinism
        PriceUpdateStrategy strategy = new com.example.tradesimulator.strategy.RandomWalkStrategy(new Random(), 0.02);
        this.marketService = new MarketService(strategy);
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
}