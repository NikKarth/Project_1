package com.example.tradesimulator.service;

import com.example.tradesimulator.model.Stock;
import com.example.tradesimulator.observer.PriceObserver;
import com.example.tradesimulator.strategy.PriceUpdateStrategy;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class MarketService {

    private final Map<String, Stock> market;
    private final List<PriceObserver> observers;
    private final PriceUpdateStrategy priceUpdateStrategy;

    public MarketService(PriceUpdateStrategy priceUpdateStrategy) {
        this.priceUpdateStrategy = priceUpdateStrategy;
        this.market = new HashMap<>();
        this.observers = new CopyOnWriteArrayList<>();

        // Initialize some hardcoded stocks
        market.put("AAPL", new Stock("AAPL", BigDecimal.valueOf(150)));
        market.put("GOOG", new Stock("GOOG", BigDecimal.valueOf(2800)));
        market.put("TSLA", new Stock("TSLA", BigDecimal.valueOf(700)));
        market.put("AMZN", new Stock("AMZN", BigDecimal.valueOf(3300)));
        market.put("MSFT", new Stock("MSFT", BigDecimal.valueOf(250)));
    }

    /** Register a new observer */
    public void registerObserver(PriceObserver observer) {
        observers.add(observer);
    }

    /** Remove an observer */
    public void removeObserver(PriceObserver observer) {
        observers.remove(observer);
    }

    /** Update stock prices and notify observers */
    public void updateMarket() {
        priceUpdateStrategy.updatePrices(market);
        for (Stock stock : market.values()) {
            notifyObservers(stock);
        }
    }

    /** Notify all observers of a price change */
    private void notifyObservers(Stock stock) {
        for (PriceObserver observer : observers) {
            observer.priceUpdated(stock);
        }
    }

    /** Get a copy of the market map */
    public Map<String, Stock> getMarket() {
        return Collections.unmodifiableMap(market);
    }
}