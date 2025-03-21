package com.example.shopappbackend.enums;

public enum ErrorCode {
    ARGUMENT_NOT_VALID("1", "輸入參數驗證失敗"),
    SERVER_ERROR("2", "伺服器錯誤"),
    UNPROCESSABLE_ENTITY("3", "邏輯錯誤")// 異常訊息由錯誤訊息組成
    ;

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }
}
