package com.example.shopappbackend.Services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.shopappbackend.dto.CartDto;
import com.example.shopappbackend.model.Cart;

@Service
public class CartService {
    private RedisTemplate<String, List<Cart>> redisTemplate;

    public CartService(RedisTemplate<String, List<Cart>> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    
    public List<CartDto> findByUserName(String username) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(username))
                .orElse(new ArrayList<>())
                .stream()
                .map(t -> new CartDto(t.getProductId(), t.getQuantity()))
                .collect(Collectors.toList());
    }

    // 更新資料如果購物車沒有會新增
    public List<CartDto> update(String username, CartDto dto) {
        List<Cart> cartList = redisTemplate.opsForValue().get(username);

        if (cartList == null) {
            cartList = new ArrayList<>();
        }

        Optional<Cart> cart = cartList.stream().filter(t -> t.getProductId().equals(dto.getProductId())).findAny();
        if (cart.isPresent()) {
            cart.get().setQuantity(cart.get().getQuantity() + dto.getQuantity());
        } else {
            Cart newCart = new Cart();
            newCart.setUsername(username);
            newCart.setProductId(dto.getProductId());
            newCart.setQuantity(dto.getQuantity());
            cartList.add(newCart);
        }
        redisTemplate.opsForValue().set(username, cartList);

        return cartList.stream()
                .map(t -> new CartDto(t.getProductId(), t.getQuantity()))
                .collect(Collectors.toList());
    }

    // 刪除單筆
    public List<CartDto> delete(String productId, String username) {
        List<Cart> cartList = redisTemplate.opsForValue().get(username);

        if (cartList == null) {
            cartList = new ArrayList<>();
        }

        cartList.removeIf(t -> t.getProductId().equals(productId));
        redisTemplate.opsForValue().set(username, cartList);

        return cartList.stream()
                .map(t -> new CartDto(t.getProductId(), t.getQuantity()))
                .collect(Collectors.toList());
    }

    // 刪除全部
    public void delete(String username) {
        redisTemplate.opsForValue().getOperations().delete(username);
    }
}
