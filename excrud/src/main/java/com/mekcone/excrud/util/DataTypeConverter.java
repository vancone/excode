package com.mekcone.excrud.util;

import com.mekcone.excrud.constant.JavaWords;
import com.mekcone.excrud.constant.SqlDataType;

public class DataTypeConverter {
    public static String convertToJavaDataType(String dataType) {
        String[] stringArray = dataType.split("\\(");
        dataType =  stringArray[0].toUpperCase();

        switch (dataType) {
            case SqlDataType.INT:
                return JavaWords.INT;

            case SqlDataType.FLOAT:
                return JavaWords.FLOAT;

            case SqlDataType.DOUBLE:
                return JavaWords.DOUBLE;

            case SqlDataType.VARCHAR:
            case SqlDataType.TEXT:
            case SqlDataType.TIMESTAMP:
                return JavaWords.STRING;

            default:
                LogUtil.warn(dataType);
                return "-";
        }
    }
}
