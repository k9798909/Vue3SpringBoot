package com.example.shopappbackend.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CartDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
}
