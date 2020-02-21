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
import model.java.annotations.SingleValueAnnotation;
import model.java.expressions.ReturnExpression;
import model.project.Project;
import model.project.Table;

public class ControllerGenerator {
    public ControllerGenerator(Project project, String path) {
        for (Table table: project.getTables()) {
            /*if (table.getPrimaryKey() == null || table.getPrimaryKey().getName().isEmpty()) {
                Logger.output(LogType.ERROR, "Mapper interface cannot be generated from a table without a primary key");
            }*/
            SourceCodeFile sourceCodeFile = new SourceCodeFile();
            sourceCodeFile.description = ProjectBuilder.getDescription();
            sourceCodeFile.packageName = project.getGroupId() + "." + project.getArtifactId().getData() + ".controller";
            sourceCodeFile.importedItems.add("java.util.List");
            sourceCodeFile.importedItems.add("org.springframework.web.bind.annotation.*");
            sourceCodeFile.importedItems.add("org.springframework.beans.factory.annotation.Autowired");
            sourceCodeFile.importedItems.add(project.getGroupId() + "." + project.getArtifactId().getData() + ".model.RestResponse");
            sourceCodeFile.importedItems.add(project.getGroupId() + "." + project.getArtifactId().getData() + ".model." + table.getName().capitalizedCamelStyle());
            sourceCodeFile.importedItems.add(project.getGroupId() + "." + project.getArtifactId().getData() + ".service." + table.getName().capitalizedCamelStyle() + "Service");

            Annotation annotation = new Annotation("RestController");
            SingleValueAnnotation singleValueAnnotation = new SingleValueAnnotation(
                    "RequestMapping",
                    "/" + table.getName().camelStyle()
            );

            Bean controllerBean = Bean.publicClass(
                    table.getName().capitalizedCamelStyle() + "Controller"
            );
            controllerBean.addAnnotation(annotation);
            controllerBean.addAnnotation(singleValueAnnotation);

            Variable service = new Variable();
            service.setName(table.getName().camelStyle() + "Service");
            service.setType(table.getName().capitalizedCamelStyle() + "Service");
            service.setAccessModifier(AccessModifier.PRIVATE);
            service.addAnnotation(new Annotation("Autowired"));
            controllerBean.addVariable(service);

            // Generate create method
            controllerBean.addMethod(Method.publicMethod(
                    new Annotation("PostMapping"),
                    "RestResponse",
                    "create",
                    new Variable[] {
                            new Variable(
                                    new Annotation("RequestBody"),
                                    table.getName().capitalizedCamelStyle(),
                                    table.getName().camelStyle()
                            )
                    },
                    new Expression[] {
                            new Expression("if (" + table.getName().camelStyle() + "Service.create(" + table.getName().camelStyle() + ")) {"),
                            new ReturnExpression("RestResponse.success()"),
                            new Expression("}"),
                            new ReturnExpression("RestResponse.fail(1, \"Create " + table.getName().camelStyle() + " failed\")")
                    }
            ));

            // Generate retrieve method
            controllerBean.addMethod(Method.publicMethod(
                    new SingleValueAnnotation("GetMapping", "/{" +
                            table.getPrimaryKey().camelStyle() + "}"
                    ),
                    "RestResponse",
                    "retrieve",
                    new Variable[] {
                            new Variable(
                                    new Annotation("PathVariable"),
                                    BasicDataType.STRING.toString(),
                                    table.getPrimaryKey().camelStyle()
                            )
                    },
                    new Expression[] {
                            new Expression("List<" + table.getName().capitalizedCamelStyle() +
                                    "> " + table.getName().camelStyle() + "List = " +
                                    table.getName().camelStyle() +
                                    "Service.retrieve(" + table.getPrimaryKey().camelStyle() +
                                    ");"
                            ),
                            new Expression("if (" + table.getName().camelStyle() + "List == null) {"),
                            new ReturnExpression("RestResponse.fail(1, \"Retrieve data failed\")"),
                            new Expression("}"),
                            new ReturnExpression("RestResponse.success(" + table.getName().camelStyle() + "List)")
                    }

            ));

            // Generate retrieve-all method
            controllerBean.addMethod(Method.publicMethod(
                    new Annotation("GetMapping"),
                    "RestResponse",
                    "retrieveAll",
                    new Expression[] {
                            new Expression("List<" + table.getName().capitalizedCamelStyle() + "> " + table.getName().camelStyle() + "List = " + table.getName().camelStyle() + "Service.retrieveAll();"),
                            new Expression("if (" + table.getName().camelStyle() + "List == null) {"),
                            new ReturnExpression("RestResponse.fail(1, \"Retrieve data failed\")"),
                            new Expression("}"),
                            new ReturnExpression("RestResponse.success(" + table.getName().camelStyle() + "List)")
                    }
            ));

            // Generate update method
            controllerBean.addMethod(Method.publicMethod(
                    new Annotation("PutMapping"),
                    "RestResponse",
                    "update",
                    new Variable[] {
                            new Variable(
                                    new Annotation("RequestBody"),
                                    table.getName().capitalizedCamelStyle(),
                                    table.getName().camelStyle()
                            )
                    },
                    new Expression[] {
                            new Expression("if (" + table.getName().camelStyle() + "Service.update(" + table.getName().camelStyle() + ")) {return RestResponse.success();}"),
                            new ReturnExpression("RestResponse.fail(1, \"Update " + table.getName().camelStyle() + " failed\")")
                    }
            ));

            // Generate delete method
            controllerBean.addMethod(Method.publicMethod(
                    new SingleValueAnnotation("DeleteMapping", "/{" +
                            table.getPrimaryKey().camelStyle() + "}"
                    ),
                    "RestResponse",
                    "delete",
                    new Variable[] {
                            new Variable(
                                    new Annotation("PathVariable"),
                                    BasicDataType.STRING.toString(),
                                    table.getPrimaryKey().camelStyle()
                            )
                    },
                    new Expression[] {
                            new Expression("if (" + table.getName().camelStyle() +
                                    "Service.delete(" + table.getPrimaryKey().camelStyle() +
                                    ")) {"
                            ),
                            new ReturnExpression("RestResponse.success()"),
                            new Expression("}"),
                            new ReturnExpression("RestResponse.fail(1, \"Delete " +
                                    table.getName().camelStyle() + " failed\")"
                            )
                    }
            ));

            sourceCodeFile.bean = controllerBean;
            String controllerPath = path + "/" + table.getName().capitalizedCamelStyle() + "Controller.java";
            FileManager.write(controllerPath, sourceCodeFile.toString());
            Logger.info("Output controller file \"" + controllerPath + "\"");
        }
    }
}
