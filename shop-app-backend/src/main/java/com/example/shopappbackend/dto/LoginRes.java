package com.example.shopappbackend.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginRes implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username; 
    private String name;
}
