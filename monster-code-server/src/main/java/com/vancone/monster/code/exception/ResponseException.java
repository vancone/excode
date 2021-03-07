package com.vancone.monster.code.exception;

import lombok.Data;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

@Data
public class ResponseException extends RuntimeException {
    private int code;

    public ResponseException(BaseEnum baseEnums) {
        super(baseEnums.getMessage());
        this.code = baseEnums.getCode();
    }
}
