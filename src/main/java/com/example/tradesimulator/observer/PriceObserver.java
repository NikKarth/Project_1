package com.example.tradesimulator.observer;

import com.example.tradesimulator.model.Stock;

public interface PriceObserver {
    /**
     * Called whenever a stock price is updated.
     *
     * @param stock the updated stock
     */
    void priceUpdated(Stock stock);
}