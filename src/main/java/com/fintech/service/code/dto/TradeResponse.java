package com.fintech.service.code.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TradeResponse {
    private Long tradeId;
    private String symbol;
    private double price;
    private int quantity;
}
