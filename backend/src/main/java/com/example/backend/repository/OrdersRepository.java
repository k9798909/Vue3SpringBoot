package com.example.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.model.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

}
