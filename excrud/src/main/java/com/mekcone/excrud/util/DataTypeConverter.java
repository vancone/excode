package com.mekcone.excrud.util;

import com.mekcone.excrud.constant.DataType;

public class DataTypeConverter {
    public static String convertToJavaDataType(String dataType) {
        String[] stringArray = dataType.split("\\(");
        dataType =  stringArray[0].toUpperCase();

        switch (dataType) {
            case DataType.SQL_INT:
                return DataType.JAVA_INT;

            case DataType.SQL_FLOAT:
                return DataType.JAVA_FLOAT;

            case DataType.SQL_DOUBLE:
                return DataType.JAVA_DOUBLE;

            case DataType.SQL_VARCHAR:
            case DataType.SQL_TEXT:
            case DataType.SQL_TIMESTAMP:
                return DataType.JAVA_STRING;

            default:
                LogUtil.warn("Invalid data type " + dataType);
                return "-";
        }
    }
}
