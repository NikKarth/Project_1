package com.example.tradesimulator.factory;

import com.example.tradesimulator.model.LimitOrder;
import com.example.tradesimulator.model.MarketOrder;
import com.example.tradesimulator.model.Order;

import java.math.BigDecimal;

public class OrderFactory {

    public enum OrderType { MARKET, LIMIT }

    /** Factory method to create orders */
    public static Order createOrder(OrderType type, String ticker, int quantity, Order.Side side, BigDecimal limitPrice) {
        switch (type) {
            case MARKET:
                return new MarketOrder(ticker, quantity, side);
            case LIMIT:
                if (limitPrice == null) throw new IllegalArgumentException("Limit price required for LimitOrder");
                return new LimitOrder(ticker, quantity, side, limitPrice);
            default:
                throw new IllegalArgumentException("Unknown order type: " + type);
        }
    }
}