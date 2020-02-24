package com.mekcone.autocrud.core.controller.generators.backend;

import com.mekcone.autocrud.core.controller.Logger;
import com.mekcone.autocrud.core.model.enums.BasicDataType;
import com.mekcone.autocrud.core.model.java.Bean;
import com.mekcone.autocrud.core.model.java.SourceCodeFile;
import com.mekcone.autocrud.core.model.java.Variable;
import com.mekcone.autocrud.core.model.java.annotations.Annotation;
import com.mekcone.autocrud.core.model.java.annotations.SingleValueAnnotation;
import com.mekcone.autocrud.core.model.java.expressions.Expression;
import com.mekcone.autocrud.core.model.java.methods.Method;
import com.mekcone.autocrud.core.model.project.Project;
import com.mekcone.autocrud.core.controller.FileManager;
import com.mekcone.autocrud.core.controller.ProjectBuilder;

public class ApplicationEntryGenerator {
    public ApplicationEntryGenerator(Project project, String path) {
        SourceCodeFile sourceCodeFile = new SourceCodeFile();
        sourceCodeFile.description = ProjectBuilder.getDescription();
        sourceCodeFile.packageName = project.getGroupId() + "." + project.getArtifactId().getData();
        sourceCodeFile.importedItems.add("org.springframework.boot.SpringApplication");
        sourceCodeFile.importedItems.add("org.springframework.boot.autoconfigure.SpringBootApplication");
        sourceCodeFile.importedItems.add("org.mybatis.spring.annotation.MapperScan");

        Bean bean = Bean.publicClass(project.getArtifactId().capitalized() + "Application");
        bean.addAnnotation(new Annotation("SpringBootApplication"));
        bean.addAnnotation(new SingleValueAnnotation("MapperScan", project.getGroupId() + "." + project.getArtifactId().getData() + ".mapper"));
        sourceCodeFile.bean = bean;

        Method method = Method.publicMethod("main");
        method.setStaticMethod(true);
        method.setReturnType(BasicDataType.VOID.toString());
        Variable variable = new Variable();
        variable.setType("String[]");
        variable.setName("args");
        method.addParam(variable);
        method.addExpression(new Expression("SpringApplication.run(" + project.getArtifactId().capitalized() + "Application.class, args);"));
        bean.addMethod(method);

        String applicationPath = path + "/" + project.getArtifactId().capitalized() + "Application.java";
        FileManager.write(applicationPath, sourceCodeFile.toString());
        Logger.info("Output application class file \"" + applicationPath + "\"");
    }
}
