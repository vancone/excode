package controller.generators.backend;

import controller.FileManager;
import controller.Logger;
import controller.ProjectBuilder;
import model.enums.AccessModifier;
import model.enums.BasicDataType;
import model.java.Bean;
import model.java.expressions.Expression;
import model.java.methods.Method;
import model.java.SourceCodeFile;
import model.java.Variable;
import model.java.annotations.Annotation;
import model.java.expressions.ReturnExpression;
import model.project.Project;
import model.project.Table;

public class ServiceImplGenerator {
    private Project project;
    private String path;

    public ServiceImplGenerator(Project project, String path) {
        this.project = project;
        this.path = path;

        for (Table table: project.getTables()) {
            /*if (table.getPrimaryKey() == null || table.getPrimaryKey().getName().isEmpty()) {
                Logger.output(LogType.ERROR, "Mapper interface cannot be generated from a table without a primary key");
            }*/
            SourceCodeFile sourceCodeFile = new SourceCodeFile();
            sourceCodeFile.description = ProjectBuilder.getDescription();
            sourceCodeFile.packageName = project.getGroupId() + "." + project.getArtifactId().getData() + ".service.impl";
            sourceCodeFile.importedItems.add("java.util.List");
            sourceCodeFile.importedItems.add("org.springframework.beans.factory.annotation.Autowired");
            sourceCodeFile.importedItems.add("org.springframework.stereotype.Service");
            sourceCodeFile.importedItems.add(project.getGroupId() + "." + project.getArtifactId().getData() + ".mapper." + table.getName().capitalizedCamelStyle() + "Mapper");
            sourceCodeFile.importedItems.add(project.getGroupId() + "." + project.getArtifactId().getData() + ".model." + table.getName().capitalizedCamelStyle());
            sourceCodeFile.importedItems.add(project.getGroupId() + "." + project.getArtifactId().getData() + ".service." + table.getName().capitalizedCamelStyle() + "Service");

            Annotation annotation = new Annotation("Service");

            Bean controllerBean = Bean.publicClass(
                    table.getName().capitalizedCamelStyle() + "ServiceImpl"
            );
            controllerBean.setImplement(table.getName().capitalizedCamelStyle() + "Service");
            controllerBean.addAnnotation(annotation);

            Variable service = new Variable();
            service.setName(table.getName().camelStyle() + "Mapper");
            service.setType(table.getName().capitalizedCamelStyle() + "Mapper");
            service.setAccessModifier(AccessModifier.PRIVATE);
            service.addAnnotation(new Annotation("Autowired"));
            controllerBean.addVariable(service);

            this.generateCreateMethod(table, controllerBean);
            this.generateRetrieveMethod(table, controllerBean);
            this.generateRetrieveAllMethod(table, controllerBean);
            this.generateUpdateMethod(table, controllerBean);
            this.generateDeleteMethod(table, controllerBean);

            sourceCodeFile.bean = controllerBean;
            String controllerPath = path + "/" + table.getName().capitalizedCamelStyle() + "ServiceImpl.java";
            FileManager.write(controllerPath, sourceCodeFile.toString());
            Logger.info("Output service implement file \"" + controllerPath + "\"");
        }
    }

    public void generateCreateMethod(Table table, Bean controllerBean) {
        Method method = Method.publicMethod("create");
        method.addAnnotation(new Annotation("Override"));
        method.setReturnType(BasicDataType.BOOLEAN.toString());
        Variable variable = new Variable();
        variable.setType(table.getName().capitalizedCamelStyle());
        variable.setName(table.getName().camelStyle());
        method.addParam(variable);
        method.setHasBody(true);
        method.addExpression(new Expression(table.getName().camelStyle() + "Mapper.create(" + table.getName().camelStyle() + ");"));
        method.addExpression(new ReturnExpression("true"));
        controllerBean.addMethod(method);
    }

    public void generateRetrieveMethod(Table table, Bean controllerBean) {
        Method method = Method.publicMethod("retrieve");
        method.addAnnotation(new Annotation("Override"));
        method.setReturnType("List<" + table.getName().capitalizedCamelStyle() + ">");
        Variable variable = new Variable();
        variable.setType(BasicDataType.STRING.toString());
        variable.setName(table.getPrimaryKey().camelStyle());
        method.addParam(variable);
        method.setHasBody(true);
        method.addExpression(new Expression("List<" +
                table.getName().capitalizedCamelStyle() + "> " +
                table.getName().camelStyle() + "List = " +
                table.getName().camelStyle() + "Mapper.retrieve(" +
                table.getPrimaryKey().camelStyle() + ");"
        ));
        method.addExpression(new Expression("return " + table.getName().camelStyle() + "List;"));
        controllerBean.addMethod(method);
    }

    public void generateRetrieveAllMethod(Table table, Bean controllerBean) {
        Method method = Method.publicMethod("retrieveAll");
        method.addAnnotation(new Annotation("Override"));
        method.setReturnType("List<" + table.getName().capitalizedCamelStyle() + ">");
        method.setHasBody(true);
        method.addExpression(new Expression("List<" +
                table.getName().capitalizedCamelStyle() + "> " +
                table.getName().camelStyle() + "List = " +
                table.getName().camelStyle() + "Mapper.retrieveAll();"
        ));
        method.addExpression(new ReturnExpression(table.getName().camelStyle() + "List"));
        controllerBean.addMethod(method);
    }

    public void generateUpdateMethod(Table table, Bean controllerBean) {
        Method method = Method.publicMethod("update");
        method.addAnnotation(new Annotation("Override"));
        //method.addAnnotation(new SingleValueAnnotation("PutMapping", "/" + table.getName().camelStyle()));
        method.setReturnType(BasicDataType.BOOLEAN.toString());
        Variable variable = new Variable();
        variable.setType(table.getName().capitalizedCamelStyle());
        variable.setName(table.getName().camelStyle());
        method.addParam(variable);
        method.setHasBody(true);
        method.addExpression(new Expression(table.getName().camelStyle() + "Mapper.update(" + table.getName().camelStyle() + ");"));
        method.addExpression(new ReturnExpression("true"));
        controllerBean.addMethod(method);
    }

    public void generateDeleteMethod(Table table, Bean controllerBean) {
        Method method = Method.publicMethod("delete");
        method.addAnnotation(new Annotation("Override"));
        method.setReturnType(BasicDataType.BOOLEAN.toString());
        Variable variable = new Variable();
        variable.setType(BasicDataType.STRING.toString());
        variable.setName(table.getPrimaryKey().camelStyle());
        method.addParam(variable);
        method.setHasBody(true);
        method.addExpression(new Expression(table.getName().camelStyle() + "Mapper.delete(" + table.getPrimaryKey().camelStyle() + ");"));
        method.addExpression(new ReturnExpression("true"));
        controllerBean.addMethod(method);
    }
}
