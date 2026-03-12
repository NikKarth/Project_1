package com.example.tradesimulator.service;

import com.example.tradesimulator.model.Order;
import com.example.tradesimulator.model.Portfolio;
import com.example.tradesimulator.model.Stock;
import com.example.tradesimulator.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderServiceTest {

    private Portfolio portfolio;
    private MarketService marketService;
    private NotificationService notificationService;
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        portfolio = new Portfolio(BigDecimal.valueOf(1000));
        Map<String, Stock> market = new HashMap<>();
        market.put("AAPL", new Stock("AAPL", BigDecimal.valueOf(100)));
        marketService = Mockito.mock(MarketService.class);
        Mockito.when(marketService.getMarket()).thenReturn(market);
        notificationService = Mockito.mock(NotificationService.class);
        orderService = new OrderService(portfolio, marketService, notificationService);
    }

    @Test
    void testMarketBuyReducesCashAndAddsShares() {
        orderService.placeMarketOrder("AAPL", 5, Order.Side.BUY);
        assertEquals(500, portfolio.getCash().intValue());
        assertEquals(5, portfolio.getShares("AAPL"));
    }

    @Test
    void testMarketSellIncreasesCashAndRemovesShares() {
        portfolio.addShares("AAPL", 5);
        orderService.placeMarketOrder("AAPL", 3, Order.Side.SELL);
        assertEquals(1300, portfolio.getCash().intValue());
        assertEquals(2, portfolio.getShares("AAPL"));
    }
}