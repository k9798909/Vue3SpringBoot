package com.example.shopappbackend.exception;

public class LogicRuntimeException extends RuntimeException {
    public LogicRuntimeException(String message) {
        super(message);
    }

    public LogicRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
