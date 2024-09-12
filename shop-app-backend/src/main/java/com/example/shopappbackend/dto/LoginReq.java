package com.example.shopappbackend.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class LoginReq implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotBlank(message = "請輸入帳號")
    private String username; 
    @NotBlank(message = "請輸入密碼")
    private String password;
}
