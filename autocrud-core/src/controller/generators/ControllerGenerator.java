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
import model.project.Project;
import model.project.Table;

public class ControllerGenerator {
    private Project project;
    private String path;

    public ControllerGenerator(Project project, String path) {
        this.project = project;
        this.path = path;

        for (Table table: project.getTables()) {
            /*if (table.getPrimaryKey() == null || table.getPrimaryKey().getName().isEmpty()) {
                Logger.output(LogType.ERROR, "Mapper interface cannot be generated from a table without a primary key");
            }*/
            SourceCodeFile sourceCodeFile = new SourceCodeFile();
            sourceCodeFile.description = ProjectBuilder.getDescription();
            sourceCodeFile.packageName = project.getGroupId() + "." + project.getArtifactId() + ".controller";
            sourceCodeFile.importedItems.add("java.util.List");
            sourceCodeFile.importedItems.add("org.springframework.web.bind.annotation.*");
            sourceCodeFile.importedItems.add("org.springframework.beans.factory.annotation.Autowired");
            sourceCodeFile.importedItems.add(project.getGroupId() + "." + project.getArtifactId() + ".model.RestResponse");
            sourceCodeFile.importedItems.add(project.getGroupId() + "." + project.getArtifactId() + ".model." + table.getName().capitalizedCamelStyle());
            sourceCodeFile.importedItems.add(project.getGroupId() + "." + project.getArtifactId() + ".mapper." + table.getName().capitalizedCamelStyle() + "Mapper");

            //Bean mapperBean = new Bean();
            Annotation annotation = new Annotation();
            annotation.setName("RestController");
            Bean controllerBean = new Bean();
            controllerBean.addAnnotation(annotation);
            controllerBean.setAccessModifier(AccessModifier.PUBLIC);
//            controllerBean.setInterface(true);
            controllerBean.setName(table.getName().capitalizedCamelStyle() + "Controller");

            Variable mapper = new Variable();
            mapper.setName(table.getName().camelStyle() + "Mapper");
            mapper.setType(table.getName().capitalizedCamelStyle() + "Mapper");
            mapper.setAccessModifier(AccessModifier.PRIVATE);
            mapper.addAnnotation(new Annotation("Autowired"));
            controllerBean.addVariable(mapper);

            this.generateCreateMethod(table, controllerBean);
            this.generateRetrieveMethod(table, controllerBean);
            this.generateUpdateMethod(table, controllerBean);
            this.generateDeleteMethod(table, controllerBean);

            sourceCodeFile.bean = controllerBean;
            String controllerPath = path + "/" + table.getName().capitalizedCamelStyle() + "Controller.java";
            FileManager.write(controllerPath, sourceCodeFile.toString());
            Logger.info("Output controller file \"" + controllerPath + "\"");
        }
    }

    public void generateCreateMethod(Table table, Bean controllerBean) {
        Method method = Method.publicMethod("create");
        method.addAnnotation(new SingleValueAnnotation("PostMapping", "/" + table.getName().camelStyle()));
        method.setReturnType("RestResponse");
        Variable variable = new Variable();
        variable.setType(table.getName().capitalizedCamelStyle());
        variable.setName(table.getName().camelStyle());
        variable.addAnnotation(new Annotation("RequestBody"));
        method.addParam(variable);
        method.setHasBody(true);
        method.addCodeLine(table.getName().camelStyle() + "Mapper.create(" + table.getName().camelStyle() + ");");
        method.addCodeLine("return RestResponse.success();");
        controllerBean.addMethod(method);
    }

    public void generateRetrieveMethod(Table table, Bean controllerBean) {
        Method method = Method.publicMethod("retrieve");
        method.addAnnotation(new SingleValueAnnotation("GetMapping", "/" + table.getName().camelStyle()));
        method.setReturnType("RestResponse");
        Variable variable = new Variable();
        variable.setType(BasicDataType.STRING.toString());
        variable.setName(table.getPrimaryKey().camelStyle());
        method.addParam(variable);
        method.setHasBody(true);
        method.addCodeLine("List<" + table.getName().capitalizedCamelStyle() + "> " + table.getName().camelStyle() + "List = " + table.getName().camelStyle() + "Mapper.retrieve(" + table.getPrimaryKey().camelStyle() + ");");
        method.addCodeLine("if (" + table.getName().camelStyle() + "List == null) {");
        method.addCodeLine("return RestResponse.fail(1, \"Fetch data failed\");");
        method.addCodeLine("}");
        method.addCodeLine("return RestResponse.success(" + table.getName().camelStyle() + "List);");
        controllerBean.addMethod(method);
    }

    public void generateUpdateMethod(Table table, Bean controllerBean) {
        Method method = Method.publicMethod("update");
        method.addAnnotation(new SingleValueAnnotation("PutMapping", "/" + table.getName().camelStyle()));
        method.setReturnType("RestResponse");
        Variable variable = new Variable();
        variable.setType(table.getName().capitalizedCamelStyle());
        variable.setName(table.getName().camelStyle());
        variable.addAnnotation(new Annotation("RequestBody"));
        method.addParam(variable);
        method.setHasBody(true);
        method.addCodeLine("if (" + table.getName().camelStyle() + "Service.update(" + table.getPrimaryKey().camelStyle() + ")) {return RestResponse.success();}");
        method.addCodeLine("return RestResponse.fail(1, \"Update " + table.getName().camelStyle() + " failed\");");
        // method.addCodeLine(table.getName().camelStyle() + "Mapper.update(" + table.getName().camelStyle() + ");;");
        // method.addCodeLine("return RestResponse.success();");
        controllerBean.addMethod(method);
    }

    public void generateDeleteMethod(Table table, Bean controllerBean) {
        Method method = Method.publicMethod("delete");
        method.addAnnotation(new SingleValueAnnotation("DeleteMapping", "/{" + table.getPrimaryKey().camelStyle() + "}"));
        method.setReturnType("RestResponse");
        Variable variable = new Variable();
        variable.setType(BasicDataType.STRING.toString());
        variable.setName(table.getPrimaryKey().camelStyle());
        variable.addAnnotation(new Annotation("PathVariable"));
        method.addParam(variable);
        method.setHasBody(true);
        method.addCodeLine("if (" + table.getName().camelStyle() + "Service.delete(" + table.getPrimaryKey().camelStyle() + ")) {return RestResponse.success();}");
        method.addCodeLine("return RestResponse.fail(1, \"Delete " + table.getName().camelStyle() + " failed\");");
        // method.addCodeLine(table.getName().camelStyle() + "Mapper.delete(" + table.getPrimaryKey().camelStyle() + ");");
        // method.addCodeLine("return RestResponse.success();");
        controllerBean.addMethod(method);
    }
}
