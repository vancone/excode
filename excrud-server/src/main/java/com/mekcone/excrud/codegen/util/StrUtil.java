package com.mekcone.excrud.codegen.util;

public class StrUtil {
    public static String camelCase(String data) {
        String[] stringArray;
        if (data.contains("_")) {
            stringArray = data.split("_");
        } else if (data.contains("-")) {
            stringArray = data.split("-");
        } else {
            return data;
        }

        if (stringArray.length > 1) {
            data = stringArray[0];
            int i = 1;
            while (i < stringArray.length) {
                data += (stringArray[i].substring(0, 1).toUpperCase() + stringArray[i].substring(1));
                i ++;
            }
        }
        return data;
    }


    public static String capitalize(String data) {
        return (data.substring(0, 1).toUpperCase() + data.substring(1));
    }

    public static String upperCamelCase(String data) {
        data = camelCase(data);
        return (data.substring(0, 1).toUpperCase() + data.substring(1));
    }
}