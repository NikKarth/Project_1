package com.example.tradesimulator.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public abstract class Order {

    public enum Side { BUY, SELL }

    protected final String ticker;
    protected final int quantity;
    protected final Side side;
    protected final LocalDateTime timestamp;
    protected BigDecimal executedPrice;

    public Order(String ticker, int quantity, Side side) {
        this.ticker = ticker;
        this.quantity = quantity;
        this.side = side;
        this.timestamp = LocalDateTime.now();
        this.executedPrice = null;
    }

    public String getTicker() {
        return ticker;
    }

    public int getQuantity() {
        return quantity;
    }

    public Side getSide() {
        return side;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public BigDecimal getExecutedPrice() {
        return executedPrice;
    }

    public void setExecutedPrice(BigDecimal executedPrice) {
        this.executedPrice = executedPrice;
    }

    /** Executes the order at the given price */
    public abstract BigDecimal execute(BigDecimal price);
}