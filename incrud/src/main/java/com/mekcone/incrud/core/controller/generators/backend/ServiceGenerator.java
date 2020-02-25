package com.mekcone.incrud.core.controller.generators.backend;

import com.mekcone.incrud.core.controller.Logger;
import com.mekcone.incrud.core.controller.ProjectBuilder;
import com.mekcone.incrud.core.model.enums.AccessModifier;
import com.mekcone.incrud.core.model.enums.BasicDataType;
import com.mekcone.incrud.core.model.java.Bean;
import com.mekcone.incrud.core.model.java.SourceCodeFile;
import com.mekcone.incrud.core.model.java.Variable;
import com.mekcone.incrud.core.model.java.methods.Method;
import com.mekcone.incrud.core.model.project.Project;
import com.mekcone.incrud.core.model.project.Table;
import com.mekcone.incrud.core.controller.FileManager;

public class ServiceGenerator {
    private Project project;
    private String path;

    public ServiceGenerator(Project project, String path) {
        this.project = project;
        this.path = path;

        for (Table table: project.getTables()) {
            /*if (table.getPrimaryKey() == null || table.getPrimaryKey().getName().isEmpty()) {
                Logger.output(LogType.ERROR, "Mapper interface cannot be generated from a table without a primary key");
            }*/
            SourceCodeFile sourceCodeFile = new SourceCodeFile();
            sourceCodeFile.description = ProjectBuilder.getDescription();
            sourceCodeFile.packageName = project.getGroupId() + "." + project.getArtifactId().getData() + ".service";
            sourceCodeFile.importedItems.add("java.util.List");
            sourceCodeFile.importedItems.add(project.getGroupId() + "." + project.getArtifactId().getData() + ".model." + table.getName().capitalizedCamelStyle());

            Bean serviceInterface = new Bean();
            serviceInterface.setName(table.getName().capitalizedCamelStyle() + "Service");
            serviceInterface.setInterface(true);
            serviceInterface.setAccessModifier(AccessModifier.PUBLIC);

            this.generateCreateMethod(table, serviceInterface);
            this.generateRetrieveMethod(table, serviceInterface);
            this.generateRetrieveAllMethod(table, serviceInterface);
            this.generateUpdateMethod(table, serviceInterface);
            this.generateDeleteMethod(table, serviceInterface);

            sourceCodeFile.bean = serviceInterface;
            String controllerPath = path + "/" + table.getName().capitalizedCamelStyle() + "Service.java";
            FileManager.write(controllerPath, sourceCodeFile.toString());
            Logger.info("Output service file \"" + controllerPath + "\"");
        }
    }

    public void generateCreateMethod(Table table, Bean controllerBean) {
        Method method = new Method();
        method.setName("create");
        method.setReturnType(BasicDataType.BOOLEAN.toString());
        Variable variable = new Variable();
        variable.setType(table.getName().capitalizedCamelStyle());
        variable.setName(table.getName().camelStyle());
        method.addParam(variable);
        method.setHasBody(false);
        controllerBean.addMethod(method);
    }

    public void generateRetrieveMethod(Table table, Bean controllerBean) {
        Method method = new Method();
        method.setName("retrieve");
        method.setReturnType("List<" + table.getName().capitalizedCamelStyle() + ">");
        Variable variable = new Variable();
        variable.setType(BasicDataType.STRING.toString());
        variable.setName(table.getPrimaryKey().camelStyle());
        method.addParam(variable);
        method.setHasBody(false);
        controllerBean.addMethod(method);
    }

    public void generateRetrieveAllMethod(Table table, Bean controllerBean) {
        Method method = new Method();
        method.setName("retrieveAll");
        method.setReturnType("List<" + table.getName().capitalizedCamelStyle() + ">");
        method.setHasBody(false);
        controllerBean.addMethod(method);
    }

    public void generateUpdateMethod(Table table, Bean controllerBean) {
        Method method = new Method();
        method.setName("update");
        method.setReturnType(BasicDataType.BOOLEAN.toString());
        Variable variable = new Variable();
        variable.setType(table.getName().capitalizedCamelStyle());
        variable.setName(table.getName().camelStyle());
        method.addParam(variable);
        method.setHasBody(false);
        controllerBean.addMethod(method);
    }

    public void generateDeleteMethod(Table table, Bean controllerBean) {
        Method method = new Method();
        method.setName("delete");
        method.setReturnType(BasicDataType.BOOLEAN.toString());
        Variable variable = new Variable();
        variable.setType(BasicDataType.STRING);
        variable.setName(table.getPrimaryKey().camelStyle());
        method.addParam(variable);
        method.setHasBody(false);
        controllerBean.addMethod(method);
    }
}
