package com.vancone.excode.generator.enums;

import com.vancone.cloud.common.exception.BaseEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Tenton Lien
 * @since 2021/11/19
 */
public enum ResponseEnum implements BaseEnum {

    /**
     * Project (10001 ~ 11000)
     */
    PROJECT_GENERATE_TASK_ALREADY_EXIST(10001, "There has been a running task"),

    /**
     * Data Store (11001 ~ 12000)
     */
    DATA_STORE_PROJECT_REQUIRED(11001, "Project ID required");

    @Getter
    @Setter
    private int code;

    @Getter @Setter
    private String message;

    ResponseEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
