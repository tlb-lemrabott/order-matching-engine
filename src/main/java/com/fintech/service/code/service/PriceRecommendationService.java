package com.fintech.service.code.service;

import com.fintech.service.code.dto.PriceRecommendationResponse;
import com.fintech.service.code.model.Order;
import com.fintech.service.code.model.OrderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PriceRecommendationService {

    private final OrderBookService orderBookService;
    private final TradeService tradeService;

    public PriceRecommendationResponse recommendSellPrice(String symbol) {
        var bestBuy = orderBookService.getBuyOrders(symbol)
                .stream()
                .max(Comparator.comparingDouble(Order::getPrice));

        var lastPrice = tradeService.getLastTradedPrice(symbol);

        double recommendedPrice = 0.0;
        String message;

        if (bestBuy.isPresent()) {
            recommendedPrice = Math.max(bestBuy.get().getPrice() + 0.01, lastPrice != null ? lastPrice : 0.0);
            message = String.format("Recommended sell price is based on the highest buy order in the market (%.2f) and recent trade price (%.2f).",
                    bestBuy.get().getPrice(), lastPrice != null ? lastPrice : 0.0);
        } else if (lastPrice != null) {
            recommendedPrice = lastPrice;
            message = String.format("No active buy orders. Using recent trade price (%.2f) as reference.", lastPrice);
        } else {
            message = "No market data available to recommend a sell price.";
            return new PriceRecommendationResponse(symbol, "SELL", message, null);
        }

        return new PriceRecommendationResponse(symbol, "SELL", message, recommendedPrice);
    }

    public PriceRecommendationResponse recommendBuyerAction(String symbol) {
        var bestBuy = orderBookService.getBuyOrders(symbol)
                .stream()
                .max(Comparator.comparingDouble(Order::getPrice));
        var bestSell = orderBookService.getSellOrders(symbol)
                .stream()
                .min(Comparator.comparingDouble(Order::getPrice));

        if (bestBuy.isEmpty() && bestSell.isPresent()) {
            return new PriceRecommendationResponse(
                    symbol,
                    "BUY",
                    "No active buy orders. Best sell available at " + bestSell.get().getPrice(),
                    bestSell.get().getPrice()
            );
        }

        if (bestSell.isEmpty() && bestBuy.isPresent()) {
            return new PriceRecommendationResponse(
                    symbol,
                    "BUY",
                    "No active sell orders. Hard to evaluate a buy recommendation.",
                    null
            );
        }

        if (bestBuy.isEmpty() && bestSell.isEmpty()) {
            return new PriceRecommendationResponse(
                    symbol,
                    "BUY",
                    "Insufficient data for market analysis. No buy or sell orders present.",
                    null
            );
        }


        double spread = bestSell.get().getPrice() - bestBuy.get().getPrice();
        String message;
        String action;

        if (spread > 0.05) {
            message = String.format("The spread between best sell (%.2f) and best buy (%.2f) is %.2f. Consider waiting or placing a competitive limit order.",
                    bestSell.get().getPrice(), bestBuy.get().getPrice(), spread);
        } else {
            message = String.format("Low spread (%.2f) between best sell (%.2f) and best buy (%.2f). It may be a good time to buy.",
                    spread, bestSell.get().getPrice(), bestBuy.get().getPrice());
        }
        return new PriceRecommendationResponse(symbol, "BUY", message, bestSell.get().getPrice());
    }

}
