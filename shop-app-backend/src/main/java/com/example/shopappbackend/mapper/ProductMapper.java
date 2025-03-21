package com.example.shopappbackend.mapper;

import com.example.shopappbackend.dto.ProductDto;
import com.example.shopappbackend.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toProductDto(Product product);
}
