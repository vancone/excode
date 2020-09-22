package com.mekcone.studio.enums;

import com.mekcone.studio.exception.BaseEnum;
import lombok.Getter;
import lombok.Setter;

/*
 * Author: Tenton Lien
 * Date: 9/21/2020
 */

public enum UserEnum implements BaseEnum {
    NOT_LOGIN_YET(1001, "Not login yet."),
    LOGIN_TIMEOUT(1002, "Login timout.");

    @Getter @Setter
    private int code;

    @Getter @Setter
    private String message;

    UserEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
