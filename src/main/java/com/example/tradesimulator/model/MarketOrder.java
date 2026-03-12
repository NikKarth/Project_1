package com.example.tradesimulator.model;

import java.math.BigDecimal;

public class MarketOrder extends Order {

    public MarketOrder(String ticker, int quantity, Side side) {
        super(ticker, quantity, side);
    }

    @Override
    public BigDecimal execute(BigDecimal price) {
        // Execution price = current price × quantity
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return "MarketOrder{" + side + " " + quantity + " of " + ticker + '}';
    }
}