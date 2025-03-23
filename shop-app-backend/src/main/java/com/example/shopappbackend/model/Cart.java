package com.example.shopappbackend.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    private String productId;
    private String username;
    private Integer quantity;
}
