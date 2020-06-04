package com.mekcone.excrud.codegen.util;

import com.mekcone.excrud.codegen.constant.DataType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataTypeConverter {
    public static String convertToJavaDataType(String dataType) {
        String[] stringArray = dataType.split("\\(");
        dataType = stringArray[0].toUpperCase();

        String javaDataType;
        switch (dataType) {
            case DataType.SQL_INT:
                javaDataType = DataType.JAVA_INT;
                break;
            case DataType.SQL_FLOAT:
                javaDataType = DataType.JAVA_FLOAT;
                break;
            case DataType.SQL_DOUBLE:
                javaDataType = DataType.JAVA_DOUBLE;
                break;
            case DataType.SQL_VARCHAR:
            case DataType.SQL_TEXT:
            case DataType.SQL_TIMESTAMP:
                javaDataType = DataType.JAVA_STRING;
                break;
            default:
                log.warn("Invalid data type " + dataType);
                javaDataType = "-";
        }
        ;
        return javaDataType;
    }
}
