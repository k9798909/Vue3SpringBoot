package com.example.shopappbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopappbackend.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {
    List<Product> findByNameContaining(String name);
}
