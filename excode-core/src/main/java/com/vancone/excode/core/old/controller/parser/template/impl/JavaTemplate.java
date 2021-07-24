package com.vancone.excode.core.old.controller.parser.template.impl;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.vancone.excode.core.old.controller.parser.template.Template;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.core.old.model.database.Table;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.util.FileUtil;
import com.vancone.excode.core.util.StrUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
@Slf4j
public class JavaTemplate implements Template {
    private String path;
    private CompilationUnit compilationUnit;

    public JavaTemplate(String path) {
        this.path = path;
        String templateText = FileUtil.read(path);
        if (templateText != null) {
            compilationUnit = StaticJavaParser.parse(templateText);
        } else {
            log.error("Parse string as a valid template failed");
        }
    }

    public boolean insert(String tag, String replacement) {
        if (compilationUnit == null) {
            log.info("Compilation unit is null: tag={}, replacement={}", tag, replacement);
            return false;
        }
        String templateText = compilationUnit.toString();
        int index = templateText.indexOf("__" + tag + "__");
        if (index < 0) {
            return false;
        }

        while (index > -1) {
            templateText = templateText.substring(0, index) + replacement +
                    templateText.substring(index + tag.length() + 4);
            index = templateText.indexOf("__" + tag + "__");
        }

        try {
            compilationUnit = StaticJavaParser.parse(templateText);
        } catch(Exception e) {
            log.error("Error parsing: " + templateText + "\n" + e.getMessage());
        }
        return true;
    }

    public boolean insertOnce(String tag, String replacement) {
        String templateText = compilationUnit.toString();
        int index = templateText.indexOf("__" + tag + "__");
        if (index < 0) {
            // LogUtil.warn("Tag \"" + tag  + "\" not found.");
            return false;
        }

        templateText = templateText.substring(0, index) + replacement +
                templateText.substring(index + tag.length() + 4);

        compilationUnit = StaticJavaParser.parse(templateText);
        return true;
    }

    public void preprocessForSpringBootProject(Project project, Table table) {
        insert("groupId", project.getGroupId());
        insert("artifactId", project.getArtifactId());
        insert("ArtifactId", StrUtil.capitalize(project.getArtifactId()));
        if (table != null) {
            insert("Table", table.getUpperCamelCaseName());
            insert("table", table.getCamelCaseName());
            if (table.getPrimaryKey() != null) {
                insert("primaryKey", table.getCamelCasePrimaryKey());
                insert("primary_key", table.getPrimaryKey());
            }
        }
    }

    public void preprocessForSpringBootProject(ProjectOld project, Table table) {
        insert("groupId", project.getGroupId());
        insert("artifactId", project.getArtifactId());
        insert("ArtifactId", StrUtil.capitalize(project.getArtifactId()));
        if (table != null) {
            insert("Table", table.getUpperCamelCaseName());
            insert("table", table.getCamelCaseName());
            if (table.getPrimaryKey() != null) {
                insert("primaryKey", table.getCamelCasePrimaryKey());
                insert("primary_key", table.getPrimaryKey());
            }
        }
    }

    public boolean remove(String tag) {
        return insert(tag, "");
    }

    public void removeImport(String importName) {
        NodeList<ImportDeclaration> imports = compilationUnit.getImports();
        if (imports != null && !imports.isEmpty()) {
            for (int i = 0; i < imports.size(); i ++) {
                if (imports.get(i).getNameAsString().contains(importName)) {
                    imports.get(i).remove();
                    i --;
                }
            }
        }
    }

    public MethodDeclaration getMethodByName(String className, String methodName) {
        ClassOrInterfaceDeclaration mainClass = compilationUnit.getClassByName(className).get();
        List<MethodDeclaration> methods = mainClass.getMethodsByName(methodName);
        if (!methods.isEmpty()) {
            return methods.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return compilationUnit.toString();
    }
}
