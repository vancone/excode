package com.vancone.excode.generator.util;

/**
 * @author Tenton Lien
 */
public class StrUtil {
    public static String toCamelCase(String str) {
        String[] stringArray;
        if (str.contains("_")) {
            stringArray = str.split("_");
        } else if (str.contains("-")) {
            stringArray = str.split("-");
        } else {
            return str;
        }

        if (stringArray.length > 1) {
            str = stringArray[0];
            int i = 1;
            StringBuilder dataBuilder = new StringBuilder(str);
            while (i < stringArray.length) {
                dataBuilder.append(stringArray[i].substring(0, 1).toUpperCase()).append(stringArray[i].substring(1));
                i ++;
            }
            str = dataBuilder.toString();
        }
        return str;
    }

    /**
     * Transform camel case into snake case
     * @param str
     * @return
     */
    public static String toSnakeCase(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i ++) {
            if (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z') {
                if (i == 0) {
                    stringBuilder.append((char)(str.charAt(i) + 32));
                } else {
                    stringBuilder.append('_').append((char)(str.charAt(i) + 32));
                }
            } else {
                stringBuilder.append(str.charAt(i));
            }
        }
        return stringBuilder.toString().toLowerCase();
    }

    /**
     * Transform kebab case into snake case
     * @param str
     * @return
     */
    public static String toKebabCase(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < str.length(); i ++) {
            if (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z') {
                if (i == 0) {
                    stringBuilder.append((char)(str.charAt(i) + 32));
                } else {
                    stringBuilder.append('-').append((char)(str.charAt(i) + 32));
                }
            } else {
                stringBuilder.append(str.charAt(i));
            }
        }
        return stringBuilder.toString().toLowerCase();
    }


    public static String capitalize(String data) {
        return (data.substring(0, 1).toUpperCase() + data.substring(1));
    }

    public static String toPascalCase(String str) {
        str = toCamelCase(str);
        return (str.substring(0, 1).toUpperCase() + str.substring(1));
    }
}