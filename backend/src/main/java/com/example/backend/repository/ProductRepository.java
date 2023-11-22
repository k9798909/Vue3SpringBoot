package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Product;

public interface ProductRepository extends JpaRepository<Product, String> {

}
