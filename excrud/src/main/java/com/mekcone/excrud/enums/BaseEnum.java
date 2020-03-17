package com.mekcone.excrud.enums;

public class BaseEnum {
    private int code;

    private String message;

    BaseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }
}
