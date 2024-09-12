package com.example.shopappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopappbackend.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

}
