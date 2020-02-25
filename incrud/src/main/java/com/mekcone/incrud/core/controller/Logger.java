package com.mekcone.incrud.core.controller;

public class Logger {
    public static void info(String message) {
        output("INFO", message);
    }

    public static void warn(String message) {
        output("WARN", message);
    }

    public static void error(String message) {
        output("ERROR", message);
        System.exit(-1);
    }

    public static void debug(String message) {
        output("DEBUG", message);
    }

    private static void output(String type, String message) {
        System.out.println(type + ": " + message);
    }
}
