package com.vancone.excode.generator.util;

/**
 * @author Tenton Lien
 */
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
            StringBuilder dataBuilder = new StringBuilder(data);
            while (i < stringArray.length) {
                dataBuilder.append(stringArray[i].substring(0, 1).toUpperCase()).append(stringArray[i].substring(1));
                i ++;
            }
            data = dataBuilder.toString();
        }
        return data;
    }

    /**
     * Transform camel case into snake case
     * @param data
     * @return
     */
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

    /**
     * Transform kebab case into snake case
     * @param data
     * @return
     */
    public static String kebabCase(String data) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < data.length(); i ++) {
            if (data.charAt(i) >= 'A' && data.charAt(i) <= 'Z') {
                if (i == 0) {
                    stringBuilder.append((char)(data.charAt(i) + 32));
                } else {
                    stringBuilder.append('-').append((char)(data.charAt(i) + 32));
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