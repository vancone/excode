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

    // Transform camel case into snake case
    public static String snakeCase(String data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length(); i ++) {
            if (data.charAt(i) >= 'A' && data.charAt(i) <= 'Z') {
                if (i == 0) {
                    stringBuilder.append((char)(data.charAt(i) + 32));
                } else {
                    stringBuilder.append('_').append((char)(data.charAt(i) + 32));
                }
            } else {
                stringBuilder.append(data.charAt(i));
            }
        }
        return stringBuilder.toString().toLowerCase();
    }


    public static String capitalize(String data) {
        return (data.substring(0, 1).toUpperCase() + data.substring(1));
    }

    public static String upperCamelCase(String data) {
        data = camelCase(data);
        return (data.substring(0, 1).toUpperCase() + data.substring(1));
    }
}