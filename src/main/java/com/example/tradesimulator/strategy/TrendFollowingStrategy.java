package com.example.tradesimulator.strategy;

import com.example.tradesimulator.model.Stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class TrendFollowingStrategy implements PriceUpdateStrategy {

    private final Map<String, List<BigDecimal>> priceHistory = new HashMap<>();
    private final int historySize = 5; // look at last 5 changes
    private final double momentumStrength = 0.03; // how much to amplify the trend

    @Override
    public void updatePrices(Map<String, Stock> market) {
        for (Map.Entry<String, Stock> entry : market.entrySet()) {
            String ticker = entry.getKey();
            Stock stock = entry.getValue();
            BigDecimal currentPrice = stock.getPrice();

            // Update history
            priceHistory.computeIfAbsent(ticker, k -> new ArrayList<>()).add(currentPrice);
            List<BigDecimal> history = priceHistory.get(ticker);
            if (history.size() > historySize + 1) {
                history.remove(0);
            }

            if (history.size() < 2) {
                // Not enough history, do small random change
                BigDecimal change = currentPrice.multiply(BigDecimal.valueOf((new Random().nextDouble() - 0.5) * 0.02));
                BigDecimal newPrice = currentPrice.add(change).setScale(2, RoundingMode.HALF_UP);
                stock.setPrice(newPrice.max(BigDecimal.valueOf(0.01)));
                continue;
            }

            // Calculate recent changes
            List<BigDecimal> changes = new ArrayList<>();
            for (int i = 1; i < history.size(); i++) {
                changes.add(history.get(i).subtract(history.get(i-1)));
            }

            // Average change (momentum)
            BigDecimal avgChange = changes.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(changes.size()), RoundingMode.HALF_UP);

            // Apply momentum: continue in the direction of average change
            BigDecimal momentumChange = avgChange.multiply(BigDecimal.valueOf(momentumStrength));
            BigDecimal newPrice = currentPrice.add(momentumChange).setScale(2, RoundingMode.HALF_UP);
            stock.setPrice(newPrice.max(BigDecimal.valueOf(0.01)));
        }
    }
}