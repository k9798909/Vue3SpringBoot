package com.example.backend.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class OrderDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long orderId;
    private String orderDate;
    private BigDecimal totalPrice;
    private String status;
    private List<OrderDetailsDto> orderDetails;
}
