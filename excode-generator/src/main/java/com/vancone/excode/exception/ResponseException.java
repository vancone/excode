package com.vancone.excode.exception;

import com.vancone.cloud.common.exception.BaseEnum;
import lombok.Data;

/**
 * @author Tenton Lien
 * @since 2020/09/20
 */
@Data
public class ResponseException extends RuntimeException {
    private int code;

    public ResponseException(BaseEnum baseEnums) {
        super(baseEnums.getMessage());
        this.code = baseEnums.getCode();
    }
}
