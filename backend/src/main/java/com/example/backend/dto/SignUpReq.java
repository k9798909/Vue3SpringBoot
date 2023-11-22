package com.example.backend.dto;

public record SignUpReq(
		String name,
		String birthday,
		String email,
		String address,
		String username,
		String password,
		String chkPassword) {
}
