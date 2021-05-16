package com.vancone.devops.codegen.model.file.springboot;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.vancone.devops.codegen.controller.parser.template.impl.JavaTemplate;
import com.vancone.devops.codegen.model.database.Table;
import com.vancone.devops.codegen.model.project.Project;
import com.vancone.devops.constant.ModuleConstant;
import lombok.Data;

@Data
public class SpringBootComponent {

    private JavaTemplate javaTemplate;

    private String name;

    private MethodDeclaration creatingMethod;
    private MethodDeclaration retrievingMethod;
    private MethodDeclaration retrievingListMethod;
    private MethodDeclaration updatingMethod;
    private MethodDeclaration deletingMethod;

    public SpringBootComponent(String templatePath, Project project, Table table) {
        javaTemplate = new JavaTemplate(templatePath);
        javaTemplate.preprocessForSpringBootProject(project, table);

        if (templatePath.toLowerCase().contains(ModuleConstant.SPRING_BOOT_COMPONENT_CONTROLLER)) {
            name = table.getUpperCamelCaseName() + "Controller";
        } else if (templatePath.toLowerCase().contains(ModuleConstant.SPRING_BOOT_COMPONENT_MAPPER)) {
            name = table.getUpperCamelCaseName() + "Mapper";
        } else if (templatePath.toLowerCase().contains(ModuleConstant.SPRING_BOOT_COMPONENT_SERVICE_IMPL.toLowerCase())) {
            name = table.getUpperCamelCaseName() + "ServiceImpl";
        } else if (templatePath.toLowerCase().contains(ModuleConstant.SPRING_BOOT_COMPONENT_SERVICE)) {
            name = table.getUpperCamelCaseName() + "Service";
        }
    }

    public void addClassAnnotation(AnnotationExpr annotationExpr) {
        CompilationUnit compilationUnit = javaTemplate.getCompilationUnit();
        ClassOrInterfaceDeclaration mainClass = compilationUnit.getClassByName(name).get();
        mainClass.addAnnotation(annotationExpr);
    }

    public void addMethodAnnotation(MethodDeclaration methodDeclaration, AnnotationExpr annotationExpr) {
        methodDeclaration.addAnnotation(annotationExpr);
    }

    public void addImport(String importItem) {
        CompilationUnit compilationUnit = javaTemplate.getCompilationUnit();
        compilationUnit.addImport(importItem);
    }

    @Override
    public String toString() {
        return javaTemplate.toString();
    }
}
