package com.example.shopappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopappbackend.model.ProductImage;

import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    Optional<ProductImage> findByProductId(String productId);
}
