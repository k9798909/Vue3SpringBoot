package com.example.shopappbackend.mapper;

import com.example.shopappbackend.dto.CartDto;
import com.example.shopappbackend.model.Cart;
import com.example.shopappbackend.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(source = "cart.productId", target = "productId")
    @Mapping(source = "cart.quantity", target = "quantity")
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.price", target = "price")
    CartDto toCartDto(Cart cart, Product product);

    Cart toCart(CartDto cartDto);
}
