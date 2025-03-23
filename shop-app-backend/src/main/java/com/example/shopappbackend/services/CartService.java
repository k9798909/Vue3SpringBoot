package com.example.shopappbackend.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.example.shopappbackend.mapper.CartMapper;
import com.example.shopappbackend.model.Product;
import com.example.shopappbackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.shopappbackend.dto.CartDto;
import com.example.shopappbackend.model.Cart;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CartService {
    private final RedisTemplate<String, List<Cart>> redisTemplate;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    /**
     * 根據username找尋購物車資料
     */
    public List<CartDto> findByUserName(String username) {
        List<Cart> carts = redisTemplate.opsForValue().get(username);
        return Optional.ofNullable(carts)
                .map(cartList -> cartList.stream()
                        .map(cart -> {
                            Product product = productRepository.findById(cart.getProductId()).orElseThrow();
                            return cartMapper.toCartDto(cart, product);
                        }).toList())
                .orElseGet(List::of);
    }

    /**
     * 將CartDto加入username的購物車裡
     */
    public void update(String username, CartDto dto) {
        final List<Cart> cartList = Optional.ofNullable(redisTemplate.opsForValue().get(username)).orElseGet(ArrayList::new);
        cartList.stream().filter(t -> t.getProductId().equals(dto.getProductId()))
                .findAny()
                .ifPresentOrElse(
                        cart -> cart.setQuantity(cart.getQuantity() + dto.getQuantity()),
                        () -> cartList.add(cartMapper.toCart(dto))
                );
        redisTemplate.opsForValue().set(username, cartList);
    }

    /**
     * 根據productId和username刪除redis內的購物車資料
     */
    public List<CartDto> delete(String productId, String username) {
        Optional.ofNullable(redisTemplate.opsForValue().get(username)).ifPresent(cartList -> {
            cartList.removeIf(t -> t.getProductId().equals(productId));
            if (cartList.isEmpty()) {
                redisTemplate.delete(username);
            } else {
                redisTemplate.opsForValue().set(username, cartList);
            }
        });
        return findByUserName(username);
    }

    /**
     * 根據username刪除redis內的所有購物車資料
     */
    public void delete(String username) {
        redisTemplate.opsForValue().getOperations().delete(username);
    }
}
