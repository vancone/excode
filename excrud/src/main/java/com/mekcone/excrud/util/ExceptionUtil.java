package com.mekcone.excrud.util;

import com.mekcone.excrud.exception.ExportException;

public class ExceptionUtil {
    public static void handle(ExportException ex) {
        LogUtil.warn(ex.getCode() + ": " + ex.getMessage());
    }
}
