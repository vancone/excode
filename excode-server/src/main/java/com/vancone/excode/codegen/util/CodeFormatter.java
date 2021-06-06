package com.vancone.excode.codegen.util;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Tenton Lien
 */
@Slf4j
public class CodeFormatter {
    public static String formatJavaCode(String code) {
        try {
            return new Formatter().formatSource(code);
        } catch (FormatterException e) {
            log.warn("Format Java source code failed.");
            return code;
        }
    }
}
