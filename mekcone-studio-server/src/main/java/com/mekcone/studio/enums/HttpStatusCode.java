package com.mekcone.studio.enums;

/*
 * Author: Tenton Lien
 * Date: 9/29/2020
 */

public enum HttpStatusCode {

    OK(200, "OK"),

    BAD_REQUEST(400, "Bad Request"),

    UNAUTHORIZED(401, "Unauthorized"),

    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),

    BAD_GATEWAY(502,"Bad Gateway");

    int code;
    String message;

    HttpStatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
