package com.mekcone.excrud.codegen.model.module.impl.springboot.component;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.mekcone.excrud.codegen.constant.SpringBootComponentType;
import com.mekcone.excrud.codegen.controller.parser.template.impl.JavaTemplate;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Table;
import com.mekcone.excrud.codegen.model.project.Project;
import com.sun.org.apache.xerces.internal.dom.ChildNode;
import lombok.Data;

import java.util.List;

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

        if (templatePath.toLowerCase().contains(SpringBootComponentType.CONTROLLER)) {
            name = table.getUpperCamelCaseName() + "Controller";
        } else if (templatePath.toLowerCase().contains(SpringBootComponentType.MAPPER)) {
            name = table.getUpperCamelCaseName() + "Mapper";
        } else if (templatePath.toLowerCase().contains(SpringBootComponentType.SERVICE_IMPL.toLowerCase())) {
            name = table.getUpperCamelCaseName() + "ServiceImpl";
        } else if (templatePath.toLowerCase().contains(SpringBootComponentType.SERVICE)) {
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
