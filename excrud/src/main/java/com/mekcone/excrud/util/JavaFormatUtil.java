package com.mekcone.excrud.util;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaFormatUtil {
    public static String format(String code) {
        try {
            return  new Formatter().formatSource(code);
        } catch (FormatterException e) {
            log.warn("Format Java source code failed.");
            return code;
        }
    }
}
