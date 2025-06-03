package com.fintech.service.code.engine;

import com.fintech.service.code.metrics.TradeMetricsService;
import com.fintech.service.code.model.Order;
import com.fintech.service.code.model.OrderType;
import com.fintech.service.code.model.Trade;
import com.fintech.service.code.repository.OrderRepository;
import com.fintech.service.code.service.OrderBookService;
import com.fintech.service.code.service.TradeService;
import com.fintech.service.code.websocket.TradeWebSocketController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

@Slf4j
@Component
@RequiredArgsConstructor
public class PriceTimeMatchingEngine implements MatchingEngine {

    private final OrderBookService orderBookService;
    private final TradeService tradeService;
    private final TradeMetricsService metricsService;
    private final TradeWebSocketController webSocketController;
    private final OrderRepository orderRepository;

    @Override
    public List<Trade> match(Order incomingOrder) {
        long start = System.currentTimeMillis();

        List<Trade> trades = new ArrayList<>();
        String symbol = incomingOrder.getSymbol();
        OrderType incomingType = incomingOrder.getType();
        PriorityBlockingQueue<Order> oppositeQueue = (incomingType == OrderType.BUY)
                ? orderBookService.getSellOrders(symbol)
                : orderBookService.getBuyOrders(symbol);

        synchronized (oppositeQueue) {
            log.info("Incoming Order: {}", incomingOrder);
            log.info("Order Type: {}", incomingOrder.getType());
            log.info("Looking for Opposite Orders: {}", oppositeQueue.size());
            oppositeQueue.forEach(order -> log.info(" → Opposite Order: {}", order));

            while (incomingOrder.getQuantity() > 0 && !oppositeQueue.isEmpty()) {
                Order topOrder = oppositeQueue.peek();
                if (topOrder == null) break;

                boolean priceMatch = (incomingType == OrderType.BUY)
                        ? incomingOrder.getPrice() >= topOrder.getPrice()
                        : incomingOrder.getPrice() <= topOrder.getPrice();

                if (!priceMatch) break;

                int tradedQuantity = Math.min(incomingOrder.getQuantity(), topOrder.getQuantity());

                Trade trade = tradeService.recordTrade(
                        symbol,
                        topOrder.getPrice(),
                        tradedQuantity,
                        incomingType == OrderType.BUY ? incomingOrder.getId() : topOrder.getId(),
                        incomingType == OrderType.SELL ? incomingOrder.getId() : topOrder.getId()
                );

                trades.add(trade);

                metricsService.incrementTradeCount();

                webSocketController.sendTradeUpdate(trade);

                incomingOrder.setQuantity(incomingOrder.getQuantity() - tradedQuantity);
                topOrder.setQuantity(topOrder.getQuantity() - tradedQuantity);

                if (topOrder.getQuantity() == 0) {
                    oppositeQueue.poll();
                    topOrder.setActive(false);
                }

                orderRepository.save(topOrder);
                orderRepository.save(incomingOrder);
            }
        }

        if (incomingOrder.getQuantity() > 0) {
            orderBookService.addOrder(incomingOrder);
        } else {
            incomingOrder.setActive(false);
        }

        long duration = System.currentTimeMillis() - start;
        metricsService.recordTradeLatency(duration);

        return trades;
    }


}

