package com.example.shopappbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ErrorDto {
    private String code;
    private String message;
    private Map<String, List<String>> fieldErrors;
}
