package com.example.tradesimulator.strategy;

import com.example.tradesimulator.model.Stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Random;

public class RandomWalkStrategy implements PriceUpdateStrategy {

    private final Random random;
    private final double maxChangePercent;

    public RandomWalkStrategy(Random random, double maxChangePercent) {
        this.random = random;
        this.maxChangePercent = maxChangePercent; // e.g., 0.02 for ±2%
    }

    @Override
    public void updatePrices(Map<String, Stock> market) {
        for (Stock stock : market.values()) {
            double change = (random.nextDouble() * 2 - 1) * maxChangePercent;
            BigDecimal oldPrice = stock.getPrice();
            BigDecimal changeAmount = oldPrice.multiply(BigDecimal.valueOf(change));
            BigDecimal newPrice = oldPrice.add(changeAmount).setScale(2, RoundingMode.HALF_UP);
            stock.setPrice(newPrice.max(BigDecimal.valueOf(0.01))); // prevent negative prices
        }
    }
}