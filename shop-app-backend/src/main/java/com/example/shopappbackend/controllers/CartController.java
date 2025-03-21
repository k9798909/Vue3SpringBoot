package com.example.shopappbackend.controllers;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopappbackend.services.CartService;
import com.example.shopappbackend.dto.CartDto;

@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CartController {
    private final CartService cartService;

    @GetMapping("/cart")
    public List<CartDto> findByUsername(Authentication authentication) {
        return cartService.findByUserName(authentication.getName());
    }

    @PostMapping("/cart")
    public void update(@RequestBody CartDto dto, Authentication authentication) {
        cartService.update(authentication.getName(), dto);
    }

    @DeleteMapping("/cart/{productId}")
    public List<CartDto> delete(@PathVariable String productId, Authentication authentication) {
        return cartService.delete(productId, authentication.getName());
    }

}
