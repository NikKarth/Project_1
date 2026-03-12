package com.example.tradesimulator.service;

import com.example.tradesimulator.model.*;
import com.example.tradesimulator.factory.OrderFactory;
import com.example.tradesimulator.notification.NotificationService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderService {

    private final Portfolio portfolio;
    private final MarketService marketService;
    private final NotificationService notificationService;

    private final List<LimitOrder> pendingLimitOrders = new ArrayList<>();
    private final List<Order> executedOrders = new ArrayList<>();

    public OrderService(Portfolio portfolio,
                        MarketService marketService,
                        NotificationService notificationService) {
        this.portfolio = portfolio;
        this.marketService = marketService;
        this.notificationService = notificationService;
    }

    /** Place a market order */
    public void placeMarketOrder(String ticker, int quantity, Order.Side side) {
        Order order = OrderFactory.createOrder(OrderFactory.OrderType.MARKET, ticker, quantity, side, null);
        BigDecimal price = marketService.getMarket().get(ticker).getPrice();
        executeOrder(order, price);
    }

    /** Place a limit order */
    public void placeLimitOrder(String ticker, int quantity, Order.Side side, BigDecimal limitPrice) {
        LimitOrder order = (LimitOrder) OrderFactory.createOrder(OrderFactory.OrderType.LIMIT, ticker, quantity, side, limitPrice);
        pendingLimitOrders.add(order);
    }

    /** Execute an order and update portfolio */
    private void executeOrder(Order order, BigDecimal price) {
        BigDecimal total = order.execute(price);
        order.setExecutedPrice(price);

        if (order.getSide() == Order.Side.BUY) {
            if (portfolio.getCash().compareTo(total) < 0) {
                notificationService.notifyOrderExecuted(order, "Insufficient funds");
                return;
            }
            portfolio.setCash(portfolio.getCash().subtract(total));
            portfolio.addShares(order.getTicker(), order.getQuantity());
        } else { // SELL
            if (portfolio.getShares(order.getTicker()) < order.getQuantity()) {
                notificationService.notifyOrderExecuted(order, "Not enough shares to sell");
                return;
            }
            portfolio.setCash(portfolio.getCash().add(total));
            portfolio.removeShares(order.getTicker(), order.getQuantity());
        }

        executedOrders.add(order);
        notificationService.notifyOrderExecuted(order, "Order executed at $" + price);
    }

    /** Check pending limit orders on each market update */
    public void checkLimitOrders() {
        Iterator<LimitOrder> iterator = pendingLimitOrders.iterator();
        while (iterator.hasNext()) {
            LimitOrder order = iterator.next();
            BigDecimal currentPrice = marketService.getMarket().get(order.getTicker()).getPrice();
            if (order.canExecute(currentPrice)) {
                executeOrder(order, currentPrice);
                iterator.remove();
            }
        }
    }

    public List<Order> getExecutedOrders() {
        return executedOrders;
    }

    public List<LimitOrder> getPendingLimitOrders() {
        return pendingLimitOrders;
    }
}