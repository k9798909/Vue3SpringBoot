package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend.model.Users;

public interface UsersRepository extends JpaRepository<Users, String> {
    List<Users> findByUsername(String username);
}