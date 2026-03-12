package com.example.tradesimulator.model;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Portfolio {
    private BigDecimal cash;
    private final Map<String, Integer> holdings;

    public Portfolio(BigDecimal initialCash) {
        this.cash = initialCash;
        this.holdings = new HashMap<>();
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public Map<String, Integer> getHoldings() {
        return holdings;
    }

    public void addShares(String ticker, int quantity) {
        holdings.put(ticker, holdings.getOrDefault(ticker, 0) + quantity);
    }

    public void removeShares(String ticker, int quantity) {
        holdings.put(ticker, holdings.getOrDefault(ticker, 0) - quantity);
        if (holdings.get(ticker) <= 0) {
            holdings.remove(ticker);
        }
    }

    public int getShares(String ticker) {
        return holdings.getOrDefault(ticker, 0);
    }

    public BigDecimal getTotalValue(Map<String, Stock> market) {
        BigDecimal total = cash;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            Stock stock = market.get(entry.getKey());
            if (stock != null) {
                total = total.add(stock.getPrice().multiply(BigDecimal.valueOf(entry.getValue())));
            }
        }
        return total;
    }
}