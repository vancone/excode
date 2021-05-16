package com.vancone.devops.enums;

import com.vancone.devops.exception.BaseEnum;
import lombok.Getter;
import lombok.Setter;

/*
 * Author: Tenton Lien
 * Date: 9/20/2020
 */

public enum ProjectEnum implements BaseEnum {
    PROJECT_NOT_EXIST(1001, "The project is not found");
    @Getter @Setter
    private int code;

    @Getter @Setter
    private String message;

    ProjectEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
