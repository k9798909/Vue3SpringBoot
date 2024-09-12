package com.example.shopappbackend.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopappbackend.Services.CartService;
import com.example.shopappbackend.Services.OrdersService;
import com.example.shopappbackend.dto.OrderDto;

@RestController
public class OrdersController {
    private OrdersService orderService;
    private CartService cartService;

    public OrdersController(OrdersService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderDto>> findAll(Authentication authentication) {
        return ResponseEntity.ok(orderService.findByUsername(authentication.getName()));
    }

    @PostMapping("/orders")
    public ResponseEntity<Void> checkOut(Authentication authentication) {
        orderService.checkOut(authentication.getName());
        cartService.delete(authentication.getName());
        return ResponseEntity.ok().build();
    }

}
