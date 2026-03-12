package com.example.tradesimulator.strategy;

import com.example.tradesimulator.model.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomWalkStrategyTest {

    @Test
    void testPriceChangeWithinBounds() {
        Map<String, Stock> market = new HashMap<>();
        market.put("AAPL", new Stock("AAPL", BigDecimal.valueOf(100)));
        RandomWalkStrategy strategy = new RandomWalkStrategy(new Random(42), 0.02);

        strategy.updatePrices(market);
        BigDecimal newPrice = market.get("AAPL").getPrice();
        assertTrue(newPrice.doubleValue() >= 98 && newPrice.doubleValue() <= 102);
    }
}