package com.example.shopappbackend.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PENDING("0", "待出貨"), SHIPPED("1", "已出貨");

    private final String code;
    private final String name;

    OrderStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public static OrderStatus fromCode(String code) {
        return switch (code) {
            case "0" -> PENDING;
            case "1" -> SHIPPED;
            default -> throw new IllegalArgumentException("Invalid code: " + code);
        };
    }

    public static String toName(String code) {
        return switch (code) {
            case "0" -> PENDING.name;
            case "1" -> SHIPPED.name;
            default -> code;
        };
    }

}
