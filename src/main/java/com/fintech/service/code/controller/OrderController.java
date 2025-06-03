package com.fintech.service.code.controller;

import com.fintech.service.code.dto.OrderRequest;
import com.fintech.service.code.dto.PriceRecommendationResponse;
import com.fintech.service.code.dto.TradeResponse;
import com.fintech.service.code.model.Trade;
import com.fintech.service.code.service.OrderService;
import com.fintech.service.code.service.PriceRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    private final PriceRecommendationService priceRecommendationService;

    @GetMapping("/recommend/sell-price")
    public ResponseEntity<PriceRecommendationResponse> recommendSellPrice(@RequestParam String symbol) {
        return ResponseEntity.ok(priceRecommendationService.recommendSellPrice(symbol));
    }

    @GetMapping("/recommend/buy-action")
    public ResponseEntity<PriceRecommendationResponse> recommendBuyAction(@RequestParam String symbol) {
        return ResponseEntity.ok(priceRecommendationService.recommendBuyerAction(symbol));
    }

    @PostMapping
    public ResponseEntity<List<TradeResponse>> placeOrder(@RequestBody OrderRequest request) {
        System.out.println(request.getSymbol());
        List<Trade> trades = orderService.placeOrder(request);
        List<TradeResponse> response = trades.stream()
                .map(t -> new TradeResponse(t.getId(), t.getSymbol(), t.getPrice(), t.getQuantity()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

}
