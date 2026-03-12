package com.example.tradesimulator.model;

import java.math.BigDecimal;

public class LimitOrder extends Order {

    private final BigDecimal limitPrice;
    private boolean executed;

    public LimitOrder(String ticker, int quantity, Side side, BigDecimal limitPrice) {
        super(ticker, quantity, side);
        this.limitPrice = limitPrice;
        this.executed = false;
    }

    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public boolean isExecuted() {
        return executed;
    }

    /** Checks if the order can execute at the given market price */
    public boolean canExecute(BigDecimal marketPrice) {
        if (executed) return false;
        if (side == Side.BUY) return marketPrice.compareTo(limitPrice) <= 0;
        else return marketPrice.compareTo(limitPrice) >= 0;
    }

    @Override
    public BigDecimal execute(BigDecimal price) {
        executed = true;
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    @Override
    public String toString() {
        return "LimitOrder{" + side + " " + quantity + " of " + ticker +
                " at limit $" + limitPrice + '}';
    }
}