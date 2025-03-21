package com.example.shopappbackend.mapper;

import com.example.shopappbackend.dto.CartDto;
import com.example.shopappbackend.model.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toCartDto(Cart cart);

    Cart toCart(CartDto cartDto);
}
