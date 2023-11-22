package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.ProductImage;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

}
