package controller.generators.backend;

import controller.FileManager;
import controller.Logger;
import controller.ProjectBuilder;
import model.enums.AccessModifier;
import model.enums.BasicDataType;
import model.java.Bean;
import model.java.expressions.Expression;
import model.java.methods.Getter;
import model.java.methods.Method;
import model.java.SourceCodeFile;
import model.java.Variable;
import model.java.expressions.ReturnExpression;
import model.java.methods.Setter;
import model.project.Column;
import model.project.Project;
import model.project.Table;

public class ModelGenerator {
    private Project project;
    private String path;

    public ModelGenerator(Project project, String path) {
        this.project = project;
        this.path = path;
        //this.generateRestResponse();

        for (Table table: project.getTables()) {
            Bean bean = new Bean(AccessModifier.PUBLIC, table.getName().capitalizedCamelStyle());
            for (Column column: table.getColumns()) {
                String type = column.getType();
                if (!type.equals("int")) {
                    type = "String";
                }
                Variable variable = new Variable(type, column.getName().camelStyle());
                variable.setAccessModifier(AccessModifier.PRIVATE);
                bean.addVariable(variable);
                bean.addMethod(new Getter(variable));
                bean.addMethod(new Setter(variable));
            }
            SourceCodeFile sourceCodeFile = new SourceCodeFile();

            sourceCodeFile.description = ProjectBuilder.getDescription();
            sourceCodeFile.packageName = project.getGroupId() + "." + project.getArtifactId().getData() + ".model";
            sourceCodeFile.bean = bean;

            String modelPath = path + "/" + bean.getName().getData() + ".java";
            FileManager.write(modelPath, sourceCodeFile.toString());
            Logger.info("Output model bean file \"" + modelPath + "\"");
        }

        // Copy template RestResponse.java
        String restResponseSource = FileManager.read(System.getProperty("user.dir") + "/templates/RestResponse.java");
        restResponseSource = "package " + project.getGroupId() + "." + project.getArtifactId().getData() + ".model;\n\n" + restResponseSource;
        String restResponsePath = path + "/RestResponse.java";
        FileManager.write(restResponsePath, restResponseSource);
        Logger.info("Copy model bean file to \"" + restResponsePath + "\"");
    }
}
