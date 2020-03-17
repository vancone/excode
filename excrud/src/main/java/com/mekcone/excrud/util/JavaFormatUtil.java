package com.mekcone.excrud.util;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

public class JavaFormatUtil {
    public static String format(String code) {
        try {
            String formattedCode = new Formatter().formatSource(code);
            return  formattedCode;
        } catch (FormatterException e) {
            LogUtil.warn("Format Java source code failed.");
            return code;
        }
    }
}
