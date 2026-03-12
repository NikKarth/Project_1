package com.example.tradesimulator.model;

import java.math.BigDecimal;

public class Stock {
    private final String ticker;
    private BigDecimal price;

    public Stock(String ticker, BigDecimal price) {
        this.ticker = ticker;
        this.price = price;
    }

    public String getTicker() {
        return ticker;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return ticker + ": $" + price;
    }
}