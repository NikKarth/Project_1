package com.example.tradesimulator.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class LimitOrderTest {

    @Test
    void testCanExecuteBuyAndSell() {
        LimitOrder buyOrder = new LimitOrder("AAPL", 5, Order.Side.BUY, BigDecimal.valueOf(100));
        LimitOrder sellOrder = new LimitOrder("AAPL", 5, Order.Side.SELL, BigDecimal.valueOf(100));

        assertTrue(buyOrder.canExecute(BigDecimal.valueOf(90)));
        assertFalse(buyOrder.canExecute(BigDecimal.valueOf(110)));

        assertTrue(sellOrder.canExecute(BigDecimal.valueOf(110)));
        assertFalse(sellOrder.canExecute(BigDecimal.valueOf(90)));
    }
}