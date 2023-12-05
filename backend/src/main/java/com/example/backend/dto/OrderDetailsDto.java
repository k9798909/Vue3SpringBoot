package com.example.backend.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderDetailsDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private ProductDto productDto;
    private Integer quantity;
    private BigDecimal price;
}
