package com.vancone.excode.generator.entity;

import com.github.javaparser.ParseProblemException;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.vancone.excode.generator.enums.TemplateType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Tenton Lien
 * @since 2021/07/24
 */
@Data
@Slf4j
@Document
public class Template {
    private TemplateType type;
    private String path;
    private String format;
    private String module;
    private String fileName;
    private String content;

    public void replace(String tag, String value) {
        content = content.replace("__" + tag + "__", value);
    }

    public CompilationUnit parseJavaSource() {
        try {
            return StaticJavaParser.parse(content);
        } catch (ParseProblemException e) {
            log.error(e.getMessage());
            log.error("content: {}", content);
            return null;
        }
    }

    public void updateJavaSource(CompilationUnit unit) {
        try {
            content = new Formatter().formatSource(unit.toString());
        } catch (FormatterException e) {
            log.warn("Failed to format Java source code.");
            content = unit.toString();
        }
    }
}
