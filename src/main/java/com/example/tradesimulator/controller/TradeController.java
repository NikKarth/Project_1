package com.example.tradesimulator.controller;

import com.example.tradesimulator.model.Order;
import com.example.tradesimulator.model.LimitOrder;
import com.example.tradesimulator.notification.ConsoleNotificationService;
import com.example.tradesimulator.notification.NotificationService;
import com.example.tradesimulator.notification.EmailNotificationDecorator;
import com.example.tradesimulator.service.MarketService;
import com.example.tradesimulator.service.OrderService;
import com.example.tradesimulator.singleton.PortfolioManager;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TradeController {

    private final MarketService marketService;
    private final OrderService orderService;

    public TradeController() {
        // Get singleton portfolio and market
        var manager = PortfolioManager.getInstance();
        this.marketService = manager.getMarketService();

        // Setup notifications with decorator pattern
        NotificationService notificationService = new ConsoleNotificationService();
        // Wrap with Email decorator for future extension
        notificationService = new EmailNotificationDecorator(notificationService);

        this.orderService = new OrderService(manager.getPortfolio(), marketService, notificationService);
    }

    /** Get current portfolio and cash */
    @GetMapping("/portfolio")
    public Map<String, Object> getPortfolio() {
        var manager = PortfolioManager.getInstance();
        return Map.of(
                "cash", manager.getPortfolio().getCash(),
                "holdings", manager.getPortfolio().getHoldings(),
                "totalValue", manager.getPortfolio().getTotalValue(marketService.getMarket())
        );
    }

    /** Get current stock prices */
    @GetMapping("/market")
    public Map<String, BigDecimal> getMarket() {
        var market = marketService.getMarket();
        return market.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getPrice()));
    }

    /** Get executed trades */
    @GetMapping("/trades")
    public List<Order> getExecutedTrades() {
        return orderService.getExecutedOrders();
    }

    /** Get pending limit orders */
    @GetMapping("/pending")
    public List<LimitOrder> getPendingLimitOrders() {
        return orderService.getPendingLimitOrders();
    }

    /** Place market order */
    @PostMapping("/order/market")
    public String placeMarketOrder(@RequestParam String ticker,
                                   @RequestParam int quantity,
                                   @RequestParam Order.Side side) {
        orderService.placeMarketOrder(ticker, quantity, side);
        return "Market order submitted";
    }

    /** Place limit order */
    @PostMapping("/order/limit")
    public String placeLimitOrder(@RequestParam String ticker,
                                  @RequestParam int quantity,
                                  @RequestParam Order.Side side,
                                  @RequestParam BigDecimal limitPrice) {
        orderService.placeLimitOrder(ticker, quantity, side, limitPrice);
        return "Limit order submitted";
    }
}