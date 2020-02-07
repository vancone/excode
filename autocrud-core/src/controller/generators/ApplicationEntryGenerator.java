package controller.generators;

import controller.FileManager;
import controller.Logger;
import controller.ProjectBuilder;
import model.enums.AccessModifier;
import model.enums.BasicDataType;
import model.lang.Bean;
import model.lang.Method;
import model.lang.SourceCodeFile;
import model.lang.Variable;
import model.lang.annotations.Annotation;
import model.lang.annotations.SingleValueAnnotation;
import model.project.Value;
import model.project.Project;

public class ApplicationEntryGenerator {
    public ApplicationEntryGenerator(Project project, String path) {
        SourceCodeFile sourceCodeFile = new SourceCodeFile();
        sourceCodeFile.description = ProjectBuilder.getDescription();
        sourceCodeFile.packageName = project.getGroupId() + "." + project.getArtifactId();
        sourceCodeFile.importedItems.add("org.springframework.boot.SpringApplication");
        sourceCodeFile.importedItems.add("org.springframework.boot.autoconfigure.SpringBootApplication");
        sourceCodeFile.importedItems.add("org.mybatis.spring.annotation.MapperScan");

        Bean bean = Bean.publicClass(Value.String(project.getArtifactId()).capitalized() + "Application");
        bean.addAnnotation(new Annotation("SpringBootApplication"));
        bean.addAnnotation(new SingleValueAnnotation("MapperScan", project.getGroupId() + "." + project.getArtifactId() + ".mapper"));
        sourceCodeFile.bean = bean;

        Method method = Method.publicMethod("main");
        method.setStaticMethod(true);
        method.setReturnType(BasicDataType.VOID.toString());
        Variable variable = new Variable();
        variable.setType("String[]");
        variable.setName("args");
        method.addParam(variable);
        method.addCodeLine("SpringApplication.run(" + Value.String(project.getArtifactId()).capitalized() + "Application.class, args);");
        bean.addMethod(method);

        String applicationPath = path + "/" + Value.String(project.getArtifactId()).capitalized() + "Application.java";
        FileManager.write(applicationPath, sourceCodeFile.toString());
        Logger.info("Output application class file \"" + applicationPath + "\"");
    }
}
