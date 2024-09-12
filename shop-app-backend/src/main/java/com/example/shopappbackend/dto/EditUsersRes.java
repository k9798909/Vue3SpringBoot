package com.example.shopappbackend.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class EditUsersRes implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String birthday;
    private String email;
    private String address;
    private String username;
}
