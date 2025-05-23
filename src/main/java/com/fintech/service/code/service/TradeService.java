package com.fintech.service.code.service;

import com.fintech.service.code.model.Trade;
import com.fintech.service.code.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final TradeRepository tradeRepository;

    public Trade recordTrade(String symbol, double price, int quantity, Long buyOrderId, Long sellOrderId) {
        Trade trade = Trade.builder()
                .symbol(symbol)
                .price(price)
                .quantity(quantity)
                .buyOrderId(buyOrderId)
                .sellOrderId(sellOrderId)
                .timestamp(Instant.now())
                .build();
        return tradeRepository.save(trade);
    }

}
