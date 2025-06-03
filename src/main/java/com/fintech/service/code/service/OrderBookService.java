package com.fintech.service.code.service;

import com.fintech.service.code.model.Order;
import com.fintech.service.code.model.OrderType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

@Slf4j
@Service
@Scope("singleton")
public class OrderBookService {

    private final Map<String, PriorityBlockingQueue<Order>> buyOrders = new ConcurrentHashMap<>();
    private final Map<String, PriorityBlockingQueue<Order>> sellOrders = new ConcurrentHashMap<>();

    public void addOrder(Order order) {
        log.info("Adding Order to Book: {}", order);
        log.info("Hash of orderBookService instance: " + this.hashCode());
        PriorityBlockingQueue<Order> queue = getQueue(order.getSymbol(), order.getType());
        queue.offer(order);
    }
    public PriorityBlockingQueue<Order> getQueue(String symbol, OrderType type) {
        if (type == OrderType.BUY) {
            return buyOrders.computeIfAbsent(symbol, k -> new PriorityBlockingQueue<>(100,
                    Comparator.<Order>comparingDouble(Order::getPrice).reversed()
                            .thenComparing(Order::getTimestamp)));
        } else {
            return sellOrders.computeIfAbsent(symbol, k -> new PriorityBlockingQueue<>(100,
                    Comparator.<Order>comparingDouble(Order::getPrice)
                            .thenComparing(Order::getTimestamp)));
        }
    }
    public PriorityBlockingQueue<Order> getBuyOrders(String symbol) {
        return buyOrders.getOrDefault(symbol, new PriorityBlockingQueue<>());
    }
    public PriorityBlockingQueue<Order> getSellOrders(String symbol) {
        return sellOrders.getOrDefault(symbol, new PriorityBlockingQueue<>());
    }
}
