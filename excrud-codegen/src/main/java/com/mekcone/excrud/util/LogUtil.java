package com.mekcone.excrud.util;

import com.mekcone.excrud.enums.ErrorEnum;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogUtil {
    public static void error(ErrorEnum errorEnum) {
        error(errorEnum, null);
    }

    public static void error(ErrorEnum errorEnum, String replacement) {
        if (replacement != null) {
            errorEnum.insertValue(replacement);
        }
        log.error("(" + errorEnum.getCode() + "): " + errorEnum.getMessage());
    }

    public static void fatalError(ErrorEnum errorEnum) {
        fatalError(errorEnum, null);
    }

    public static void fatalError(ErrorEnum errorEnum, String replacement) {
        error(errorEnum, replacement);
        System.exit(-1);
    }

    public static void info(String message) {
        log.info(message);
    }
}
