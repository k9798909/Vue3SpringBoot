package com.example.backend.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderDto(
                Long orderId,
                String orderDate,
                BigDecimal totalPrice,
                String status,
                List<OrderDetailsDto> orderDetails) {
}
