package com.example.tradesimulator.controller;

import com.example.tradesimulator.model.Order;
import com.example.tradesimulator.model.LimitOrder;
import com.example.tradesimulator.service.MarketService;
import com.example.tradesimulator.service.OrderService;
import com.example.tradesimulator.singleton.PortfolioManager;
import com.example.tradesimulator.notification.NotificationService;
import com.example.tradesimulator.notification.ConsoleNotificationService;
import com.example.tradesimulator.notification.EmailNotificationDecorator;
import com.example.tradesimulator.notification.SMSNotificationDecorator;
import com.example.tradesimulator.notification.DashboardNotificationDecorator;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TradeController {

    private final PortfolioManager portfolioManager;

    public TradeController() {
        this.portfolioManager = PortfolioManager.getInstance();
    }

    /** Get current portfolio and cash */
    @GetMapping("/portfolio")
    public Map<String, Object> getPortfolio(@RequestParam(defaultValue = "user1") String user) {
        var portfolio = portfolioManager.getPortfolio(user);
        if (portfolio == null) return Map.of("error", "User not found");
        return Map.of(
                "cash", portfolio.getCash(),
                "holdings", portfolio.getHoldings(),
                "totalValue", portfolio.getTotalValue(portfolioManager.getMarketService().getMarket())
        );
    }

    /** Get current stock prices */
    @GetMapping("/market")
    public Map<String, BigDecimal> getMarket() {
        var market = portfolioManager.getMarketService().getMarket();
        Map<String, BigDecimal> prices = new HashMap<>();
        for (var entry : market.entrySet()) {
            prices.put(entry.getKey(), entry.getValue().getPrice());
        }
        return prices;
    }

    /** Get executed trades */
    @GetMapping("/trades")
    public List<Order> getExecutedTrades(@RequestParam(defaultValue = "user1") String user) {
        var orderService = portfolioManager.getOrderService(user);
        return orderService != null ? orderService.getExecutedOrders() : List.of();
    }

    /** Get pending limit orders */
    @GetMapping("/pending")
    public List<LimitOrder> getPendingLimitOrders(@RequestParam(defaultValue = "user1") String user) {
        var orderService = portfolioManager.getOrderService(user);
        return orderService != null ? orderService.getPendingLimitOrders() : List.of();
    }

    /** Place market order */
    @PostMapping("/order/market")
    public String placeMarketOrder(@RequestParam String ticker,
                                   @RequestParam int quantity,
                                   @RequestParam Order.Side side,
                                   @RequestParam(defaultValue = "user1") String user) {
        var orderService = portfolioManager.getOrderService(user);
        if (orderService == null) return "User not found";
        orderService.placeMarketOrder(ticker, quantity, side);
        return "Market order submitted";
    }

    /** Place limit order */
    @PostMapping("/order/limit")
    public String placeLimitOrder(@RequestParam String ticker,
                                  @RequestParam int quantity,
                                  @RequestParam Order.Side side,
                                  @RequestParam BigDecimal limitPrice,
                                  @RequestParam(defaultValue = "user1") String user) {
        var orderService = portfolioManager.getOrderService(user);
        if (orderService == null) return "User not found";
        orderService.placeLimitOrder(ticker, quantity, side, limitPrice);
        return "Limit order submitted";
    }

    /** Get dashboard badge counter */
    @GetMapping("/badge")
    public int getBadgeCounter(@RequestParam(defaultValue = "user1") String user) {
        // For now, return 0 since dashboard decorator counter not implemented
        return 0;
    }

    /** Get available strategies */
    @GetMapping("/strategies")
    public List<String> getAvailableStrategies() {
        return List.of("random-walk", "mean-reversion", "trend-following");
    }

    /** Set pricing strategy */
    @PostMapping("/strategy")
    public String setStrategy(@RequestParam String strategyName) {
        portfolioManager.getMarketService().setStrategy(strategyName);
        return "Strategy set to " + strategyName;
    }

    /** Set notification channels */
    @PostMapping("/notification")
    public String setNotification(@RequestParam(defaultValue = "user1") String user, @RequestParam List<String> channels) {
        NotificationService service = new ConsoleNotificationService();
        if (channels.contains("email")) {
            service = new EmailNotificationDecorator(service);
        }
        if (channels.contains("sms")) {
            service = new SMSNotificationDecorator(service);
        }
        if (channels.contains("dashboard")) {
            service = new DashboardNotificationDecorator(service);
        }
        portfolioManager.setNotificationForUser(user, service);
        return "Notifications set for " + user;
    }
}