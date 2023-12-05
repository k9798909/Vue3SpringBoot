package com.example.backend.model;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Cart implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private String productId;
    private Integer quantity;
}
