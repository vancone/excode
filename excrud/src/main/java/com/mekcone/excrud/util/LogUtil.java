package com.mekcone.excrud.util;

import com.mekcone.excrud.enums.BaseEnum;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LogUtil {
    private static StringProperty logConsoleText = new SimpleStringProperty();

    public static StringProperty logConsoleTextProperty() {
        return logConsoleText;
    }

    public static void clearConsole() {
        logConsoleText.setValue("");
    }

    public static void info(String message) {
        println("INFO", message);
    }

    public static void warn(String message) {
        println("WARN", message);
    }

    /*public static void error(String message) {
        println("ERROR", message);
        System.exit(-1);
    }*/

    public static void error(BaseEnum baseEnum) {
        println("ERROR(" + baseEnum.getCode() + "): " + baseEnum.getMessage());
        System.exit(-1);
    }

    public static void error(int code , String message) {
        println("ERROR(" + code + "): " + message);
        System.exit(-1);
    }

    public static void debug(String message) {
        println("DEBUG", message);
    }

    public static void print(String message) {
        System.out.print(message);
        if (logConsoleText.getValue() == null) {
            logConsoleText.setValue(message);
        } else {
            logConsoleText.setValue(logConsoleText.getValue() + message);
        }
    }

    public static void println(String message) {
        System.out.println(message);
        if (logConsoleText.getValue() == null) {
            logConsoleText.setValue(message + "\n");
        } else {
            logConsoleText.setValue(logConsoleText.getValue() + message + "\n");
        }
    }

    private static void println(String type, String message) {
        println(type + ": " + message);
    }
}
