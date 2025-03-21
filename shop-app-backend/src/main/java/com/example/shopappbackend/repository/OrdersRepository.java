package com.example.shopappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopappbackend.model.Orders;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUserId(String userId);
}
