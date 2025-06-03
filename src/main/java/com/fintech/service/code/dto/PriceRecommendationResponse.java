package com.fintech.service.code.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceRecommendationResponse {
    private String symbol;
    private String recommendationType;
    private String message;
    private Double recommendedPrice;
}
