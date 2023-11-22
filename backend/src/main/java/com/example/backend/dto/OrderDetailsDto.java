package com.example.backend.dto;

import java.math.BigDecimal;

public record OrderDetailsDto(
                ProductDto productDto,
                Integer quantity,
                BigDecimal price) {
}
