package com.vancone.excode.core.extension;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.vancone.excode.core.ProjectWriter;
import com.vancone.excode.core.enums.TemplateType;
import com.vancone.excode.core.util.CompilationUnitUtil;

/**
 * @author Tenton Lien
 * @date 7/24/2021
 */
public class SpringBootExtensionHandler {
    public static void lombok(ProjectWriter writer) {
        for (ProjectWriter.Output output: writer.getOutputsByType(TemplateType.SPRING_BOOT_ENTITY)) {
            CompilationUnit unit = StaticJavaParser.parse(output.getContent());
            ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);

            // Add annotation
            unit.addImport("lombok.Data");
            clazz.addAnnotation(new MarkerAnnotationExpr("Data"));

            // Remove getters and setters
            for (MethodDeclaration method: clazz.getMethods()) {
                String name = method.getName().toString();
                if (name.contains("get") || name.contains("set")) {
                    clazz.remove(method);
                }
            }

            output.setContent(unit.toString());
        }
    }
}
