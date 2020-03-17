package com.mekcone.excrud.exception;

import com.mekcone.excrud.enums.BaseEnum;

public class ExportException extends RuntimeException {
    private int code;

    public ExportException(BaseEnum baseEnum) {
        super(baseEnum.getMessage());
        this.code = baseEnum.getCode();
    }

    public int getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
