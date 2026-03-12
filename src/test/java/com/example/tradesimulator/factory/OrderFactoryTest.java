package com.example.tradesimulator.factory;

import com.example.tradesimulator.model.LimitOrder;
import com.example.tradesimulator.model.MarketOrder;
import com.example.tradesimulator.model.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderFactoryTest {

    @Test
    void testMarketOrderCreation() {
        Order order = OrderFactory.createOrder(OrderFactory.OrderType.MARKET, "AAPL", 5, Order.Side.BUY, null);
        assertTrue(order instanceof MarketOrder);
    }

    @Test
    void testLimitOrderCreation() {
        Order order = OrderFactory.createOrder(OrderFactory.OrderType.LIMIT, "AAPL", 5, Order.Side.BUY, BigDecimal.valueOf(100));
        assertTrue(order instanceof LimitOrder);
    }
}