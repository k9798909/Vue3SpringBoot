package com.example.shopappbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopappbackend.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
