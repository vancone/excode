package com.vancone.excode.generator.exception;

import lombok.Data;

/**
 * @author Tenton Lien
 * @date 9/20/2020
 */
@Data
public class ResponseException extends RuntimeException {
    private int code;

    public ResponseException(BaseEnum baseEnums) {
        super(baseEnums.getMessage());
        this.code = baseEnums.getCode();
    }
}
