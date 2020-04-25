package com.mekcone.excrud.util;

import com.mekcone.excrud.constant.basic.DataType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataTypeConverter {
    public static String convertToJavaDataType(String dataType) {
        String[] stringArray = dataType.split("\\(");
        dataType =  stringArray[0].toUpperCase();

        return switch (dataType) {
            case DataType.SQL_INT -> DataType.JAVA_INT;
            case DataType.SQL_FLOAT -> DataType.JAVA_FLOAT;
            case DataType.SQL_DOUBLE -> DataType.JAVA_DOUBLE;
            case DataType.SQL_VARCHAR, DataType.SQL_TEXT, DataType.SQL_TIMESTAMP -> DataType.JAVA_STRING;
            default -> {
                log.warn("Invalid data type " + dataType);
                yield "-";
            }
        };
    }
}
