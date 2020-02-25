package com.mekcone.incrud.core.controller.generators.backend;

import com.mekcone.incrud.core.controller.Logger;
import com.mekcone.incrud.core.controller.ProjectBuilder;
import com.mekcone.incrud.core.model.enums.AccessModifier;
import com.mekcone.incrud.core.model.java.Bean;
import com.mekcone.incrud.core.model.java.SourceCodeFile;
import com.mekcone.incrud.core.model.java.Variable;
import com.mekcone.incrud.core.model.java.methods.Getter;
import com.mekcone.incrud.core.model.java.methods.Setter;
import com.mekcone.incrud.core.model.project.Column;
import com.mekcone.incrud.core.model.project.Project;
import com.mekcone.incrud.core.model.project.Table;
import com.mekcone.incrud.core.controller.FileManager;

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
