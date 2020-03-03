package com.mekcone.incrud.service.impl;

import com.mekcone.incrud.model.enums.AccessModifier;
import com.mekcone.incrud.model.enums.BasicDataType;
import com.mekcone.incrud.model.file.javalang.JavaSourceFileModel;
import com.mekcone.incrud.model.file.javalang.components.Bean;
import com.mekcone.incrud.model.file.javalang.components.Variable;
import com.mekcone.incrud.model.file.javalang.components.KeyValueAnnotation;
import com.mekcone.incrud.model.file.javalang.components.ParentAnnotation;
import com.mekcone.incrud.model.file.properties.ApplicationPropertiesFileModel;
import com.mekcone.incrud.model.file.sql.SqlFileModel;
import com.mekcone.incrud.model.file.xml.PomXmlFileModel;
import com.mekcone.incrud.model.project.ProjectModel;
import com.mekcone.incrud.model.project.components.Column;
import com.mekcone.incrud.model.project.components.Table;
import com.mekcone.incrud.service.ProjectModelService;
import com.mekcone.incrud.service.ProjectService;
import com.mekcone.incrud.util.FileUtil;
import com.mekcone.incrud.util.LogUtil;
import com.mekcone.incrud.util.PathUtil;
import com.mekcone.incrud.util.StringUtil;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.mekcone.incrud.model.file.javalang.components.Annotation;
import com.mekcone.incrud.model.file.javalang.components.Expression;
import com.mekcone.incrud.model.file.javalang.components.Method;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpringBootProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectModelService projectModelService;

    private String springBootProjectPath;

    @Override
    public boolean build() {
        try {
            String cmd = "mvn ";
            String param = PathUtil.getPath(springBootProjectPath);
            Process child = Runtime.getRuntime().exec(cmd + param);
            InputStream child_in = child.getInputStream();
            int c;
            while ((c = child_in.read()) != -1) {
                //   System.out.println("kkk");
                System.out.print((char)c);
                LogUtil.print(String.valueOf((char)c));
            }
            child_in.close();
        } catch (IOException ex) {
            LogUtil.warn(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean generate() {
        springBootProjectPath = PathUtil.getPath("EXPORT_PATH") + "/" + projectModelService.getProjectModel().getArtifactId() + "-backend/";
        String resourcesPath = springBootProjectPath + "src/main/resources/";
        String sourcePath = springBootProjectPath + "src/main/java/" + (projectModelService.getProjectModel().getGroupId() + "." + projectModelService.getProjectModel().getArtifactId()).replace('.', '/') + "/";

        FileUtil.checkDirectory(resourcesPath);
        FileUtil.checkDirectory(sourcePath);

        FileUtil.write(springBootProjectPath + "pom.xml", stringifyPomXml());
        FileUtil.write(resourcesPath + "application.properties", stringifyApplicationProperties());
        FileUtil.write(sourcePath + StringUtil.capitalizedCamel( projectModelService.getProjectModel().getArtifactId()) + "Application.java", stringifyApplicationEntry());
        FileUtil.write(springBootProjectPath + projectModelService.getProjectModel().getArtifactId() + ".sql", stringifySql());

        // Mybatis mappers
        String mybatisMapperPath = sourcePath + "mapper/";
        FileUtil.checkDirectory(mybatisMapperPath);
        for (Pair<String, String> mybatisMapperPair: stringifyMybatisMappers()) {
            FileUtil.write(mybatisMapperPath + mybatisMapperPair.getKey() +  "Mapper.java", mybatisMapperPair.getValue());
        }

        // Models
        String modelPath = sourcePath + "model/";
        FileUtil.checkDirectory(modelPath);
        for (Pair<String, String> modelPair: stringifyModels()) {
            FileUtil.write(modelPath + modelPair.getKey() +  ".java", modelPair.getValue());
        }

        // Controllers
        String controllerPath = sourcePath + "controller/";
        FileUtil.checkDirectory(controllerPath);
        for (Pair<String, String> controllerPair: stringifyControllers()) {
            FileUtil.write(controllerPath + controllerPair.getKey() +  "Controller.java", controllerPair.getValue());
        }

        // Services
        String servicePath = sourcePath + "service/";
        FileUtil.checkDirectory(servicePath);
        for (Pair<String, String> servicePair: stringifyServices()) {
            FileUtil.write(servicePath + servicePair.getKey() +  "Service.java", servicePair.getValue());
        }

        // ServiceImpl
        String serviceImplPath = sourcePath + "service/impl/";
        FileUtil.checkDirectory(serviceImplPath);
        for (Pair<String, String> serviceImplPair: stringifyServiceImpls()) {
            FileUtil.write(serviceImplPath + serviceImplPair.getKey() +  "ServiceImpl.java", serviceImplPair.getValue());
        }
        return true;
    }

    @Override
    public boolean run() {
        return false;
    }

    public String stringifyApplicationEntry() {
        ProjectModel projectModel = projectModelService.getProjectModel();
        String groupId = projectModel.getGroupId();
        String artifactId = projectModel.getArtifactId();

        JavaSourceFileModel applicationEntryFileModel = new JavaSourceFileModel();
        applicationEntryFileModel.setPackageName(groupId + "." + artifactId);

        // Imported items
        applicationEntryFileModel.addImportedItem("org.springframework.boot.SpringApplication");
        applicationEntryFileModel.addImportedItem("org.springframework.boot.autoconfigure.SpringBootApplication");
        applicationEntryFileModel.addImportedItem("org.mybatis.spring.annotation.MapperScan");

        // Main class
        String applicationName = StringUtil.capitalize(artifactId) + "Application";
        String mapperScanPath = groupId + "." + artifactId + ".mapper";

        Bean bean = Bean.publicClass(applicationName);
        bean.addAnnotation(Annotation.simpleAnnotation("SpringBootApplication"));
        bean.addAnnotation(Annotation.singleValueAnnotation("MapperScan", mapperScanPath));
        applicationEntryFileModel.setBean(bean);

        Method method = Method.publicMethod("main");
        method.setStaticMethod(true);
        method.setReturnType(BasicDataType.VOID.toString());
        Variable variable = new Variable();
        variable.setType("String[]");
        variable.setName("args");
        method.addParam(variable);
        method.addExpression(new Expression("SpringApplication.run(" + applicationName + ".class, args);"));
        bean.addMethod(method);

        return applicationEntryFileModel.toString();
    }

    public String stringifyApplicationProperties() {
        ApplicationPropertiesFileModel applicationPropertiesFileModel = new ApplicationPropertiesFileModel();
        applicationPropertiesFileModel.setDependencies(projectModelService.getProjectModel().getDependencies());
        return applicationPropertiesFileModel.toString();
    }

    public List<Pair<String, String>> stringifyControllers() {
        ProjectModel projectModel = projectModelService.getProjectModel();
        String groupId = projectModel.getGroupId();
        String artifactId = projectModel.getArtifactId();
        List<Pair<String, String>> controllerPairs = new ArrayList<>();
        for (Table table: projectModel.getTables()) {
            JavaSourceFileModel controllerFileModel = new JavaSourceFileModel();
            controllerFileModel.setPackageName(groupId + "." + artifactId + ".controller");

            controllerFileModel.addImportedItem("java.util.List");
            controllerFileModel.addImportedItem("org.springframework.web.bind.annotation.*");
            controllerFileModel.addImportedItem("org.springframework.beans.factory.annotation.Autowired");
            controllerFileModel.addImportedItem(groupId + "." + artifactId + ".model.RestResponse");
            controllerFileModel.addImportedItem(groupId + "." + artifactId + ".model." + StringUtil.capitalizedCamel(table.getName()));
            controllerFileModel.addImportedItem(groupId + "." + artifactId + ".service." + StringUtil.capitalizedCamel(table.getName()) + "Service");

            Annotation annotation = new Annotation("RestController");
            Annotation singleValueAnnotation =
                    Annotation.singleValueAnnotation("RequestMapping", "/" + StringUtil.camel(table.getName()));

            Bean controllerBean = Bean.publicClass(
                    StringUtil.capitalizedCamel(table.getName()) + "Controller"
            );
            controllerBean.addAnnotation(annotation);
            controllerBean.addAnnotation(singleValueAnnotation);

            Variable service = new Variable();
            service.setName(StringUtil.camel(table.getName()) + "Service");
            service.setType(StringUtil.capitalizedCamel(table.getName()) + "Service");
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
                                    StringUtil.capitalizedCamel(table.getName()),
                                    StringUtil.camel(table.getName())
                            )
                    },
                    new Expression[] {
                            new Expression("if (" + StringUtil.camel(table.getName()) + "Service.create(" + StringUtil.camel(table.getName()) + ")) {"),
                            Expression.returnExpression(new Expression("RestResponse.success()")),
                            new Expression("}"),
                            Expression.returnExpression(new Expression("RestResponse.fail(1, \"Create " + StringUtil.camel(table.getName()) + " failed\")"))
                    }
            ));

            // Generate retrieve method
            controllerBean.addMethod(Method.publicMethod(
                    Annotation.singleValueAnnotation("GetMapping", "/{" + table.getCamelPrimaryKey() + "}"),
                    "RestResponse",
                    "retrieve",
                    new Variable[] {
                            new Variable(
                                    new Annotation("PathVariable"),
                                    BasicDataType.STRING.toString(),
                                    table.getCamelPrimaryKey()
                            )
                    },
                    new Expression[] {
                            new Expression("List<" + table.getCapitalizedCamelName() +
                                    "> " + StringUtil.camel(table.getName()) + "List = " +
                                    StringUtil.camel(table.getName()) +
                                    "Service.retrieve(" + table.getCamelPrimaryKey() +
                                    ");"
                            ),
                            new Expression("if (" + StringUtil.camel(table.getName()) + "List == null) {"),
                            Expression.returnExpression(
                                    Expression.simpleExpression("RestResponse.fail(1, \"Retrieve data failed\")")),
                            new Expression("}"),
                            Expression.returnExpression(
                                    Expression.simpleExpression("RestResponse.success(" + StringUtil.camel(table.getName()) + "List)"))
                    }

            ));

            // Generate retrieve-all method
            controllerBean.addMethod(Method.publicMethod(
                    new Annotation("GetMapping"),
                    "RestResponse",
                    "retrieveAll",
                    new Expression[] {
                            new Expression("List<" + table.getCapitalizedCamelName() + "> " + table.getCamelName() + "List = " + table.getCamelName() + "Service.retrieveAll();"),
                            new Expression("if (" + table.getCamelName() + "List == null) {"),
                            Expression.returnExpression(Expression.simpleExpression("RestResponse.fail(1, \"Retrieve data failed\")")),
                            new Expression("}"),
                            Expression.returnExpression(Expression.simpleExpression("RestResponse.success(" + table.getCamelName() + "List)"))
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
                                    table.getCapitalizedCamelName(),
                                    StringUtil.camel(table.getName())
                            )
                    },
                    new Expression[] {
                            new Expression("if (" + StringUtil.camel(table.getName()) + "Service.update(" + StringUtil.camel(table.getName()) + ")) {return RestResponse.success();}"),
                            Expression.returnExpression(Expression.simpleExpression("RestResponse.fail(1, \"Update " + StringUtil.camel(table.getName()) + " failed\")"))
                            // Expression.returnExpression(Expression.simpleExpression())

                    }
            ));

            // Generate delete method
            controllerBean.addMethod(Method.publicMethod(
                    Annotation.singleValueAnnotation("DeleteMapping", "/{" + table.getCamelPrimaryKey() + "}"),
                    "RestResponse",
                    "delete",
                    new Variable[] {
                            new Variable(
                                    new Annotation("PathVariable"),
                                    BasicDataType.STRING.toString(),
                                    table.getCamelPrimaryKey()
                            )
                    },
                    new Expression[] {
                            new Expression("if (" + table.getCamelName() +
                                    "Service.delete(" + table.getCamelPrimaryKey() +
                                    ")) {"
                            ),
                            Expression.returnExpression(Expression.simpleExpression("RestResponse.success()")),
                            new Expression("}"),
                            Expression.returnExpression(
                                    Expression.simpleExpression("RestResponse.fail(1, \"Delete " +
                                            table.getCamelName() + " failed\")")
                            )
                    }
            ));

            controllerFileModel.setBean(controllerBean);
            controllerPairs.add(new Pair<>(table.getCapitalizedCamelName(), controllerFileModel.toString()));
        }
        return controllerPairs;
    }

    public List<Pair<String, String>> stringifyModels() {
        ProjectModel projectModel = projectModelService.getProjectModel();
        String groupId = projectModel.getGroupId();
        String artifactId = projectModel.getArtifactId();
        List<Pair<String, String>> modelPairs = new ArrayList<>();
        for (Table table: projectModelService.getProjectModel().getTables()) {
            JavaSourceFileModel modelFileModel = new JavaSourceFileModel();

            Bean bean = new Bean(AccessModifier.PUBLIC, table.getCapitalizedCamelName());
            for (Column column: table.getColumns()) {
                String type = column.getType();
                if (!type.equals("int")) {
                    type = "String";
                }

                Variable variable = Variable.privateVariable(type, column.getCamelName());
                bean.addVariable(variable);
                bean.addMethod(Method.getter(variable));
                bean.addMethod(Method.setter(variable));
            }

            modelFileModel.setPackageName(groupId + "." + artifactId + ".model");
            modelFileModel.setBean(bean);

            modelPairs.add(new Pair<>(StringUtil.capitalizedCamel(table.getName()), modelFileModel.toString()));
        }

        // Copy template RestResponse.java
        String restResponseSource = FileUtil.read(System.getProperty("user.dir") + "/templates/spring-boot-project/RestResponse.java");
        restResponseSource = "package " + groupId + "." + artifactId + ".model;\n\n" + restResponseSource;
        modelPairs.add(new Pair<>("RestResponse", restResponseSource));
        return modelPairs;
    }

    public List<Pair<String, String>> stringifyMybatisMappers() {
        ProjectModel projectModel = projectModelService.getProjectModel();
        String groupId = projectModel.getGroupId();
        String artifactId = projectModel.getArtifactId();
        List<Pair<String, String>> mybatisMapperPairs = new ArrayList<>();
        for (Table table: projectModel.getTables()) {
            if (table.isPrimaryKeyBlank()) {
                LogUtil.warn("Mapper interface cannot be generated from a table without a primary key");
            }
            JavaSourceFileModel mybatisMapperFileModel = new JavaSourceFileModel();
            mybatisMapperFileModel.setPackageName(groupId + "." + artifactId + ".mapper");
            mybatisMapperFileModel.addImportedItem("org.apache.ibatis.annotations.*");
            mybatisMapperFileModel.addImportedItem("org.springframework.stereotype.Service");
            mybatisMapperFileModel.addImportedItem("java.util.List");
            mybatisMapperFileModel.addImportedItem(groupId + "." + artifactId +
                    ".model." + table.getCapitalizedCamelName());

            Annotation annotation = new Annotation();
            annotation.setName("Service");
            Bean mapperBean = new Bean();
            mapperBean.addAnnotation(annotation);
            mapperBean.setAccessModifier(AccessModifier.PUBLIC);
            mapperBean.setInterface(true);
            mapperBean.setName(table.getCapitalizedCamelName() + "Mapper");

            // CREATE
            Method method = new Method();
            String value = "";
            value = "INSERT INTO " + table.getName() + "(";
            for (int i = 0; i < table.getColumns().size(); i ++) {
                if (!table.getColumns().get(i).isPrimaryKey()) {
                    value += table.getColumns().get(i).getName();
                    if (i + 1 != table.getColumns().size()) {
                        value += ", ";
                    } else {
                        value += ") ";
                    }
                }
            }
            value += "VALUES(";

            for (int i = 0; i < table.getColumns().size(); i ++) {
                if (!table.getColumns().get(i).isPrimaryKey()) {
                    value += "#{" + table.getColumns().get(i).getCamelName() + "}";
                    if (i + 1 != table.getColumns().size()) {
                        value += ", ";
                    } else {
                        value += ")";
                    }
                }
            }

            KeyValueAnnotation keyValueAnnotation = new KeyValueAnnotation();
            keyValueAnnotation.setName("Options");
            keyValueAnnotation.addKeyValue("useGeneratedKeys", "true");
            keyValueAnnotation.addKeyValue("keyProperty", table.getCamelPrimaryKey());
            keyValueAnnotation.addKeyValue("keyColumn", table.getPrimaryKey());
            method.addAnnotation(Annotation.singleValueAnnotation("Insert", value));
            method.addAnnotation(keyValueAnnotation);
            method.setReturnType(BasicDataType.VOID.toString());
            method.setName("create");
            Variable variable = Variable.simpleVariable(table.getCapitalizedCamelName(), table.getCamelName());
            method.addParam(variable);
            method.setHasBody(false);
            mapperBean.addMethod(method);

            // RETRIEVE
            method = new Method();
            method.addAnnotation(Annotation.singleValueAnnotation(
                    "Select", "SELECT * FROM " + table.getName() + " WHERE " + table.getPrimaryKey() +
                            " = #{" + table.getCamelPrimaryKey() +"}")
            );
            ParentAnnotation parentAnnotation = new ParentAnnotation();
            parentAnnotation.setName("Results");
            for (Column column: table.getColumns()) {
                keyValueAnnotation = new KeyValueAnnotation();
                keyValueAnnotation.setName("Result");
                keyValueAnnotation.addKeyValue("property", column.getCamelName());
                keyValueAnnotation.addKeyValue("column", column.getName());
                parentAnnotation.addAnnatation(keyValueAnnotation);
            }
            method.addAnnotation(parentAnnotation);

            method.setReturnType("List<" + table.getCapitalizedCamelName() + ">");
            method.setName("retrieve");
            method.addParam(Variable.simpleVariable(BasicDataType.STRING.toString(), table.getCamelPrimaryKey()));
            method.setHasBody(false);
            mapperBean.addMethod(method);

            // RETRIEVE ALL
            method = new Method();
            method.addAnnotation(Annotation.singleValueAnnotation("Select", "SELECT * FROM " + table.getName()));
            method.addAnnotation(parentAnnotation);
            method.setReturnType("List<" + table.getCapitalizedCamelName() + ">");
            method.setName("retrieveAll");
            method.setHasBody(false);
            mapperBean.addMethod(method);

            // UPDATE
            method = new Method();
            value = "UPDATE " + table.getName() + " SET ";
            for (int i = 0; i < table.getColumns().size(); i ++) {
                if (table.getColumns().get(i).isPrimaryKey()) {
                    continue;
                }
                String name = table.getColumns().get(i).getName();
                value += name + " = #{" + StringUtil.camel(name) + "}";
                if (i + 1 != table.getColumns().size()) {
                    value += ", ";
                }
            }
            value += " WHERE " + table.getPrimaryKey() + " = #{" + table.getCamelPrimaryKey() + "}";
            method.addAnnotation(Annotation.singleValueAnnotation("Update", value));
            method.setReturnType(BasicDataType.VOID.toString());
            method.setName("update");
            method.addParam(Variable.simpleVariable(table.getCapitalizedCamelName(), table.getCamelName()));
            method.setHasBody(false);
            mapperBean.addMethod(method);

            // DELETE
            method = new Method();
            method.addAnnotation(Annotation.singleValueAnnotation(
                    "Delete",
                    "DELETE FROM " + table.getName() + " WHERE " + table.getPrimaryKey() + " = #{" + table.getCamelPrimaryKey() +"}"
            ));
            method.setReturnType(BasicDataType.VOID.toString());
            method.setName("delete");
            method.addParam(Variable.simpleVariable(BasicDataType.STRING.toString(), table.getCamelPrimaryKey()));
            method.setHasBody(false);
            mapperBean.addMethod(method);

            mybatisMapperFileModel.setBean(mapperBean);
            // mybatisMapperFileModels.add(javaSourceFileModel);
            //String mapperPath = path + "/" + table.getCapitalizedCamelName() + "Mapper.java";
            //FileUtil.write(mapperPath, javaSourceFileModel.toString());
            mybatisMapperPairs.add(new Pair<>(StringUtil.capitalizedCamel(table.getName()), mybatisMapperFileModel.toString()));
        }
        return mybatisMapperPairs;
    }

    public String stringifyPomXml() {
        PomXmlFileModel pomXmlFileModel = new PomXmlFileModel();
        ProjectModel projectModel = projectModelService.getProjectModel();
        pomXmlFileModel.setGroupId(projectModel.getGroupId());
        pomXmlFileModel.setArtifactId(projectModel.getArtifactId());
        pomXmlFileModel.setVersion(projectModel.getVersion());
        pomXmlFileModel.setDependencies(projectModel.getDependencies());
        return pomXmlFileModel.toString();
    }

    public List<Pair<String, String>> stringifyServices() {
        ProjectModel projectModel = projectModelService.getProjectModel();
        String groupId = projectModel.getGroupId();
        String artifactId = projectModel.getArtifactId();
        List<Pair<String, String>> servicePairs = new ArrayList<>();
        for (Table table: projectModel.getTables()) {
            JavaSourceFileModel serviceFileModel = new JavaSourceFileModel();

            serviceFileModel.setPackageName(groupId + "." + artifactId + ".service");
            serviceFileModel.addImportedItem("java.util.List");
            serviceFileModel.addImportedItem(groupId + "." + artifactId + ".model." + table.getCapitalizedCamelName());

            Bean serviceInterface = new Bean();
            serviceInterface.setName(table.getCapitalizedCamelName() + "Service");
            serviceInterface.setInterface(true);
            serviceInterface.setAccessModifier(AccessModifier.PUBLIC);

            // CREATE
            Method method = new Method();
            method.setName("create");
            method.setReturnType(BasicDataType.BOOLEAN.toString());
            method.addParam(Variable.simpleVariable(table.getCapitalizedCamelName(), table.getCamelName()));
            method.setHasBody(false);
            serviceInterface.addMethod(method);

            // RETRIEVE
            method = new Method();
            method.setName("retrieve");
            method.setReturnType("List<" + table.getCapitalizedCamelName() + ">");
            method.addParam(Variable.simpleVariable(BasicDataType.STRING.toString(), table.getCamelPrimaryKey()));
            method.setHasBody(false);
            serviceInterface.addMethod(method);

            // RETRIEVE-ALL
            method = new Method();
            method.setName("retrieveAll");
            method.setReturnType("List<" + table.getCapitalizedCamelName() + ">");
            method.setHasBody(false);
            serviceInterface.addMethod(method);

            // UPDATE
            method = new Method();
            method.setName("update");
            method.setReturnType(BasicDataType.BOOLEAN.toString());
            method.addParam(Variable.simpleVariable(table.getCapitalizedCamelName(), table.getCamelName()));
            method.setHasBody(false);
            serviceInterface.addMethod(method);

            // DELETE
            method = new Method();
            method.setName("delete");
            method.setReturnType(BasicDataType.BOOLEAN.toString());
            method.addParam(Variable.simpleVariable(BasicDataType.STRING.toString(), table.getCamelPrimaryKey()));
            method.setHasBody(false);
            serviceInterface.addMethod(method);

            serviceFileModel.setBean(serviceInterface);

            servicePairs.add(new Pair<>(table.getCapitalizedCamelName(), serviceFileModel.toString()));
        }
        return servicePairs;
    }

    public List<Pair<String, String>> stringifyServiceImpls() {
        ProjectModel projectModel = projectModelService.getProjectModel();
        String groupId = projectModel.getGroupId();
        String artifactId = projectModel.getArtifactId();
        List<Pair<String, String>> serviceImplPairs = new ArrayList<>();
        for (Table table: projectModel.getTables()) {
            JavaSourceFileModel serviceImplFileModel = new JavaSourceFileModel();

            serviceImplFileModel.setPackageName(groupId + "." + artifactId + ".service.impl");
            serviceImplFileModel.addImportedItem("java.util.List");
            serviceImplFileModel.addImportedItem("org.springframework.beans.factory.annotation.Autowired");
            serviceImplFileModel.addImportedItem("org.springframework.stereotype.Service");
            serviceImplFileModel.addImportedItem(groupId + "." + artifactId + ".mapper." + table.getCapitalizedCamelName() + "Mapper");
            serviceImplFileModel.addImportedItem(groupId + "." + artifactId + ".model." + table.getCapitalizedCamelName());
            serviceImplFileModel.addImportedItem(groupId + "." + artifactId + ".service." + table.getCapitalizedCamelName() + "Service");

            Annotation annotation = new Annotation("Service");

            Bean serviceImplBean = Bean.publicClass(
                    table.getCapitalizedCamelName() + "ServiceImpl"
            );
            serviceImplBean.setImplement(table.getCapitalizedCamelName() + "Service");
            serviceImplBean.addAnnotation(annotation);

            Variable variable = Variable.privateVariable(table.getCapitalizedCamelName() + "Mapper", table.getCamelName() + "Mapper");
            variable.addAnnotation(new Annotation("Autowired"));
            serviceImplBean.addVariable(variable);

            // CREATE
            Method method = Method.publicMethod("create");
            method.addAnnotation(new Annotation("Override"));
            method.setReturnType(BasicDataType.BOOLEAN.toString());
            method.addParam(Variable.simpleVariable(table.getCapitalizedCamelName(), table.getCamelName()));
            method.setHasBody(true);
            method.addExpression(new Expression(table.getCamelName() + "Mapper.create(" + table.getCamelName() + ");"));
            method.addExpression(Expression.returnExpression(Expression.simpleExpression("true")));
            serviceImplBean.addMethod(method);

            // RETRIEVE
            method = Method.publicMethod("retrieve");
            method.addAnnotation(new Annotation("Override"));
            method.setReturnType("List<" + table.getCapitalizedCamelName() + ">");
            method.addParam(Variable.simpleVariable(BasicDataType.STRING.toString(), table.getCamelPrimaryKey()));
            method.setHasBody(true);
            method.addExpression(new Expression("List<" +
                    table.getCapitalizedCamelName() + "> " +
                    table.getCamelName() + "List = " +
                    table.getCamelName() + "Mapper.retrieve(" +
                    table.getCamelPrimaryKey() + ");"
            ));
            method.addExpression(new Expression("return " + table.getCamelName() + "List;"));
            serviceImplBean.addMethod(method);

            // RETRIEVE-ALL
            method = Method.publicMethod("retrieveAll");
            method.addAnnotation(new Annotation("Override"));
            method.setReturnType("List<" + table.getCapitalizedCamelName() + ">");
            method.setHasBody(true);
            method.addExpression(new Expression("List<" +
                    table.getCapitalizedCamelName() + "> " +
                    table.getCamelName() + "List = " +
                    table.getCamelName() + "Mapper.retrieveAll();"
            ));
            method.addExpression(Expression.returnExpression(Expression.simpleExpression(table.getCamelName() + "List")));
            serviceImplBean.addMethod(method);

            // UPDATE
            method = Method.publicMethod("update");
            method.addAnnotation(Annotation.simpleAnnotation("Override"));
            //method.addAnnotation(new SingleValueAnnotation("PutMapping", "/" + table.getCamelName()));
            method.setReturnType(BasicDataType.BOOLEAN.toString());
            method.addParam(Variable.simpleVariable(table.getCapitalizedCamelName(), table.getCamelName()));
            method.setHasBody(true);
            method.addExpression(new Expression(table.getCamelName() + "Mapper.update(" + table.getCamelName() + ");"));
            method.addExpression(Expression.returnExpression(Expression.simpleExpression("true")));
            serviceImplBean.addMethod(method);

            // DELETE
            method = Method.publicMethod("delete");
            method.addAnnotation(new Annotation("Override"));
            method.setReturnType(BasicDataType.BOOLEAN.toString());
            method.addParam(Variable.simpleVariable(BasicDataType.STRING.toString(), table.getCamelPrimaryKey()));
            method.setHasBody(true);
            method.addExpression(new Expression(table.getCamelName() + "Mapper.delete(" + table.getCamelPrimaryKey() + ");"));
            method.addExpression(Expression.returnExpression(Expression.simpleExpression("true")));
            serviceImplBean.addMethod(method);

            serviceImplFileModel.setBean(serviceImplBean);
            serviceImplPairs.add(new Pair<>(table.getCapitalizedCamelName(), serviceImplFileModel.toString()));
        }
        return serviceImplPairs;
    }

    public String stringifySql() {
        SqlFileModel sqlFileModel = new SqlFileModel();
        sqlFileModel.setTables(projectModelService.getProjectModel().getTables());
        return sqlFileModel.toString();
    }
}
