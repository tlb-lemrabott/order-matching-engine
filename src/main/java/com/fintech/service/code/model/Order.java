package com.fintech.service.code.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String symbol;
    @Enumerated(EnumType.STRING)
    private OrderType type;
    private double price;
    private int quantity;
    private Instant timestamp;
    private boolean isActive;


}

