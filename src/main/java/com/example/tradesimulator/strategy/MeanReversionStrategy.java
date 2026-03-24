package com.example.tradesimulator.strategy;

import com.example.tradesimulator.model.Stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MeanReversionStrategy implements PriceUpdateStrategy {

    private final Map<String, List<BigDecimal>> priceHistory = new HashMap<>();
    private final int historySize = 10; // moving average of last 10 prices
    private final double reversionStrength = 0.05; // how strongly it pulls towards mean

    @Override
    public void updatePrices(Map<String, Stock> market) {
        for (Map.Entry<String, Stock> entry : market.entrySet()) {
            String ticker = entry.getKey();
            Stock stock = entry.getValue();
            BigDecimal currentPrice = stock.getPrice();

            // Update history
            priceHistory.computeIfAbsent(ticker, k -> new ArrayList<>()).add(currentPrice);
            List<BigDecimal> history = priceHistory.get(ticker);
            if (history.size() > historySize) {
                history.remove(0);
            }

            // Calculate moving average
            BigDecimal sum = history.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal average = sum.divide(BigDecimal.valueOf(history.size()), RoundingMode.HALF_UP);

            // Calculate deviation from mean
            BigDecimal deviation = currentPrice.subtract(average);

            // Apply reversion: move towards mean by a fraction of the deviation
            BigDecimal change = deviation.multiply(BigDecimal.valueOf(reversionStrength));
            BigDecimal newPrice = currentPrice.subtract(change).setScale(2, RoundingMode.HALF_UP);
            stock.setPrice(newPrice.max(BigDecimal.valueOf(0.01)));
        }
    }
}