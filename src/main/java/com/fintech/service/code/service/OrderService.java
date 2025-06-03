package com.fintech.service.code.service;

import com.fintech.service.code.dto.OrderRequest;
import com.fintech.service.code.engine.MatchingEngine;
import com.fintech.service.code.model.Order;
import com.fintech.service.code.model.Trade;
import com.fintech.service.code.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MatchingEngine matchingEngine;
    private final OrderBookService orderBookService;

    @Transactional
    public List<Trade> placeOrder(OrderRequest request) {
        Order order = Order.builder()
                .symbol(request.getSymbol())
                .type(request.getType())
                .price(request.getPrice())
                .quantity(request.getQuantity())
                .timestamp(Instant.now())
                .isActive(true)
                .build();
        order = orderRepository.save(order);

        List<Trade> trades = matchingEngine.match(order);

        orderRepository.save(order);

        return trades;
    }

}
