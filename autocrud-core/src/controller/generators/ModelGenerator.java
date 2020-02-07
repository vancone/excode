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
import model.project.Column;
import model.project.Project;
import model.project.Table;

public class ModelGenerator {
    private Project project;
    private String path;

    public ModelGenerator(Project project, String path) {
        this.project = project;
        this.path = path;
        this.generateRestResponse();

        for (Table table: project.getTables()) {
            Bean bean = new Bean(AccessModifier.PUBLIC, table.getName().capitalizedCamelStyle());
            for (Column column: table.getColumns()) {
                bean.addVariable(Variable.BasicVariable(BasicDataType.STRING, column.getName().camelStyle()));
            }
            SourceCodeFile sourceCodeFile = new SourceCodeFile();

            sourceCodeFile.description = ProjectBuilder.getDescription();
            sourceCodeFile.packageName = project.getGroupId() + "." + project.getArtifactId() + ".model";
            sourceCodeFile.bean = bean;

            String modelPath = path + "/" + bean.getName().getData() + ".java";
            FileManager.write(modelPath, sourceCodeFile.toString());
            Logger.info("Output model bean file \"" + modelPath + "\"");
        }
    }

    private void generateRestResponse() {
        Bean bean = Bean.publicClass("RestResponse");
        bean.addVariable(Variable.BasicVariable(BasicDataType.INT, "code"));
        bean.addVariable(Variable.BasicVariable(BasicDataType.STRING, "message"));
        bean.addVariable(Variable.BasicVariable(BasicDataType.OBJECT, "data"));

        Method method = Method.publicMethod("success");
        method.setStaticMethod(true);
        method.setReturnType("RestResponse");
        method.addCodeLine("RestResponse restResponse = new RestResponse();");
        method.addCodeLine("restResponse.setCode(0);");
        method.addCodeLine("return restResponse;");
        bean.addMethod(method);

        Method method2 = Method.publicMethod("success");
        method2.setStaticMethod(true);
        method2.setReturnType("RestResponse");
        Variable variable = new Variable();
        variable.setType("Object");
        variable.setName("data");
        method2.addParam(variable);
        method2.addCodeLine("RestResponse restResponse = new RestResponse();");
        method2.addCodeLine("restResponse.setCode(0);");
        method2.addCodeLine("restResponse.setData(data);");
        method2.addCodeLine("return restResponse;");
        bean.addMethod(method2);

        Method method3 = new Method();
        method3.setAccessModifier(AccessModifier.PUBLIC);
        method3.setStaticMethod(true);
        method3.setReturnType("RestResponse");
        method3.setName("success");
        Variable variable2 = new Variable();
        variable2.setType("String");
        variable2.setName("message");
        method3.addParam(variable2);
        method3.addCodeLine("RestResponse restResponse = new RestResponse();");
        method3.addCodeLine("restResponse.setCode(0);");
        method3.addCodeLine("restResponse.setMessage(message);");
        method3.addCodeLine("return restResponse;");
        bean.addMethod(method3);

        Method method4 = new Method();
        method4.setAccessModifier(AccessModifier.PUBLIC);
        method4.setStaticMethod(true);
        method4.setReturnType("RestResponse");
        method4.setName("success");
        Variable variable3 = new Variable();
        variable3.setType("String");
        variable3.setName("message");
        method4.addParam(variable3);
        Variable variable4 = new Variable();
        variable4.setType("Object");
        variable4.setName("data");
        method4.addParam(variable4);
        method4.addCodeLine("RestResponse restResponse = new RestResponse();");
        method4.addCodeLine("restResponse.setCode(0);");
        method4.addCodeLine("restResponse.setMessage(message);");
        method4.addCodeLine("restResponse.setData(data);");
        method4.addCodeLine("return restResponse;");
        bean.addMethod(method4);

        Method method5 = new Method();
        method5.setAccessModifier(AccessModifier.PUBLIC);
        method5.setStaticMethod(true);
        method5.setReturnType("RestResponse");
        method5.setName("fail");
        Variable variable5 = new Variable();
        variable5.setType("int");
        variable5.setName("code");
        method5.addParam(variable5);
        method5.addCodeLine("RestResponse restResponse = new RestResponse();");
        method5.addCodeLine("restResponse.setCode(code);");
        method5.addCodeLine("return restResponse;");
        bean.addMethod(method5);

        Method method6 = new Method();
        method6.setAccessModifier(AccessModifier.PUBLIC);
        method6.setStaticMethod(true);
        method6.setReturnType("RestResponse");
        method6.setName("fail");
        Variable variable6 = new Variable();
        variable6.setType("int");
        variable6.setName("code");
        method6.addParam(variable6);
        Variable variable7 = new Variable();
        variable7.setType("String");
        variable7.setName("message");
        method6.addParam(variable7);
        method6.addCodeLine("RestResponse restResponse = new RestResponse();");
        method6.addCodeLine("restResponse.setCode(code);");
        method6.addCodeLine("restResponse.setMessage(message);");
        method6.addCodeLine("return restResponse;");
        bean.addMethod(method6);


        SourceCodeFile sourceCodeFile = new SourceCodeFile();

        sourceCodeFile.description = ProjectBuilder.getDescription();
        sourceCodeFile.packageName = project.getGroupId() + "." + project.getArtifactId() + ".model";
        sourceCodeFile.bean = bean;

        String modelPath = path + "/" + bean.getName().getData() + ".java";
        FileManager.write(modelPath, sourceCodeFile.toString());
        Logger.info("Output model bean file \"" + modelPath + "\"");
    }
}
