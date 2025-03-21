package com.example.shopappbackend.controllers;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopappbackend.services.CartService;
import com.example.shopappbackend.services.OrdersService;
import com.example.shopappbackend.dto.OrderDto;

@RestController
@RequiredArgsConstructor
public class OrdersController {
    private final OrdersService orderService;
    private final CartService cartService;

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
