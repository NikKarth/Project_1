package com.example.tradesimulator.strategy;

import com.example.tradesimulator.model.Stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

public class MeanReversionStrategy implements PriceUpdateStrategy {

    private final Random random = new Random();
    private final Map<String, List<BigDecimal>> priceHistory = new HashMap<>();
    private final int historySize = 10;

    @Override
    public void updatePrices(Map<String, Stock> market) {
        for (Map.Entry<String, Stock> entry : market.entrySet()) {
            String ticker = entry.getKey();
            Stock stock = entry.getValue();
            BigDecimal currentPrice = stock.getPrice();

            // Maintain price history
            priceHistory.computeIfAbsent(ticker, k -> new ArrayList<>()).add(currentPrice);
            List<BigDecimal> history = priceHistory.get(ticker);

            if (history.size() > historySize) {
                history.remove(0);
            }

            // Calculate moving average
            BigDecimal sum = history.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal average = sum.divide(
                    BigDecimal.valueOf(history.size()),
                    2,
                    RoundingMode.HALF_UP
            );

            // Add Gaussian noise (std dev = 0.5)
            double noise = random.nextGaussian() * 0.5;
            BigDecimal noiseBD = BigDecimal.valueOf(noise);

            // New price = mean + noise
            BigDecimal newPrice = average.add(noiseBD)
                    .setScale(2, RoundingMode.HALF_UP);

            // Prevent negative prices
            stock.setPrice(newPrice.max(BigDecimal.valueOf(0.01)));
        }
    }