package com.vancone.excode.core.model;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.vancone.excode.core.enums.TemplateType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Tenton Lien
 * @since 7/24/2021
 */
@Data
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
        return StaticJavaParser.parse(content);
    }

    public void updateJavaSource(CompilationUnit unit) {
        content = unit.toString();
    }
}
