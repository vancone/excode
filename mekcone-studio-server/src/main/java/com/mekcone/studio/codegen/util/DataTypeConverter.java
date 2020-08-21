package com.mekcone.studio.codegen.util;

import com.mekcone.studio.codegen.constant.DataType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataTypeConverter {
    public static String convertToJavaDataType(String dataType) {
        String[] stringArray = dataType.split("\\(");
        dataType = stringArray[0].toUpperCase();

        String javaDataType;
        switch (dataType) {
            case DataType.SQL_INT_INT:
                javaDataType = DataType.JAVA_INT;
                break;
            case DataType.SQL_REAL_FLOAT:
                javaDataType = DataType.JAVA_FLOAT;
                break;
            case DataType.SQL_REAL_DOUBLE:
                javaDataType = DataType.JAVA_DOUBLE;
                break;
            case DataType.SQL_TXT_VARCHAR:
            case DataType.SQL_TXT_TEXT:
            case DataType.SQL_TIME_TIMESTAMP:
                javaDataType = DataType.JAVA_STRING;
                break;
            default:
                log.warn("Invalid data type " + dataType);
                javaDataType = "-";
        }

        return javaDataType;
    }
}
