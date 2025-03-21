package com.example.shopappbackend.exception;

import org.springframework.validation.BindingResult;

public class CustomArgumentNotValidException extends RuntimeException {
    private final BindingResult bindingResult;

    public CustomArgumentNotValidException(String message, BindingResult bindingResult) {
        super(message);
        this.bindingResult = bindingResult;
    }

    public final BindingResult getBindingResult() {
        return this.bindingResult;
    }

}
