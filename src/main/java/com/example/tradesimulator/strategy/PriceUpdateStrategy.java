package com.example.tradesimulator.strategy;

import com.example.tradesimulator.model.Stock;

import java.util.Map;

public interface PriceUpdateStrategy {
    /**
     * Updates the prices of all stocks in the market.
     *
     * @param market map of ticker to Stock objects
     */
    void updatePrices(Map<String, Stock> market);
}