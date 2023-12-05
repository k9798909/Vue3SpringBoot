package com.example.backend.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CartDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String productId;
    private Integer quantity;

    public CartDto(String productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }
}
