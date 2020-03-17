package com.mekcone.mall.enums;

import lombok.Getter;

@Getter
public class BaseEnums {
    private int code;
    private String message;

    BaseEnums(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
