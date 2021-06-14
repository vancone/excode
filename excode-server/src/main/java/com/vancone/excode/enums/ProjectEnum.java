package com.vancone.excode.enums;

import com.vancone.excode.exception.BaseEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tenton Lien
 * @ate 9/20/2020
 */
public enum ProjectEnum implements BaseEnum {
    PROJECT_NOT_EXIST(1001, "The project is not found"),
    DATA_SOURCE_NOT_EXIST(1002, "The data source is not found"),
    TEST_CONNECTION_FAILED(1003, "Test data source connection failed");

    @Getter @Setter
    private int code;

    @Getter @Setter
    private String message;

    ProjectEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
