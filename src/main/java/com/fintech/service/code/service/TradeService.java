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

    public Double getLastTradedPrice(String symbol) {
        return tradeRepository.findTopBySymbolOrderByTimestampDesc(symbol)
                .map(Trade::getPrice)
                .orElse(null);
    }

    public Double getAveragePrice(String symbol, int lastN) {
        var trades = tradeRepository.findTopNBySymbolOrderByTimestampDesc(symbol, lastN);
        return trades.stream()
                .mapToDouble(Trade::getPrice)
                .average()
                .orElse(0.0);
    }


}
