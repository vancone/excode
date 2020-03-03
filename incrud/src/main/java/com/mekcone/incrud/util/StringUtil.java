package com.mekcone.incrud.util;

public class StringUtil {
    public static String camel(String data) {
        String[] stringArray = data.split("_");
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


    public static String capitalizedCamel(String data) {
        data = camel(data);
        return (data.substring(0, 1).toUpperCase() + data.substring(1));
    }
}
