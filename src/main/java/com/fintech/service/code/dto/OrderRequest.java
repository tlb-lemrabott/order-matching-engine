package com.fintech.service.code.dto;

import com.fintech.service.code.model.OrderType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private String symbol;
    private OrderType type;
    private double price;
    private int quantity;
}
