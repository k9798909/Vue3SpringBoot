package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
