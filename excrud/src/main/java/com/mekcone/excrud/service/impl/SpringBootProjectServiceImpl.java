package com.mekcone.excrud.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import com.mekcone.excrud.model.Template;
import com.mekcone.excrud.model.enums.AccessModifier;
import com.mekcone.excrud.model.enums.BasicDataType;
import com.mekcone.excrud.model.file.javalang.JavaSourceFileModel;
import com.mekcone.excrud.model.file.javalang.components.*;
import com.mekcone.excrud.model.file.sql.SqlFileModel;
import com.mekcone.excrud.model.file.xml.PomXmlFileModel;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.components.*;
import com.mekcone.excrud.service.ProjectModelService;
import com.mekcone.excrud.service.ProjectService;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.PathUtil;
import com.mekcone.excrud.util.StringUtil;
import javafx.application.Platform;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class SpringBootProjectServiceImpl implements ProjectService {

    private final String EXPORT_TYPE = "spring-boot-project";

    @Autowired
    private ProjectModelService projectModelService;

    private String springBootProjectPath;

    private Export export;

    private Runnable buildRunnable = new Runnable() {
        @Override
        public void run() {
            File file = new File(springBootProjectPath);

            // Compiling
            try {
                Process child = Runtime.getRuntime().exec("mvn.cmd clean compile", null, file);

                InputStream child_in = child.getInputStream();
                int c;
                while (true) {
                    if (!((c = child_in.read()) != -1)) break;
                    final int d = c;
                    Platform.runLater(() -> LogUtil.print(String.valueOf((char) d)));
                }
            } catch (IOException e) {
                LogUtil.warn("Compiling project failed");
                return;
            }

            // Packaging
            try {
                Process child = Runtime.getRuntime().exec("mvn.cmd clean package", null, file);

                InputStream child_in = child.getInputStream();
                int c;
                while (true) {
                    if (!((c = child_in.read()) != -1)) break;
                    final int d = c;
                    Platform.runLater(() -> LogUtil.print(String.valueOf((char) d)));
                }
            } catch (IOException e) {
                LogUtil.warn("Packaging project failed");
            }
        }
    };

    private Runnable runRunnable = new Runnable() {
        @Override
        public void run() {
            File file = new File(springBootProjectPath + "target/");

            // Running
            try {
                Process child = Runtime.getRuntime().exec("java -jar " + projectModelService.getProject().getArtifactId() + "-" + projectModelService.getProject().getVersion() + ".jar", null, file);

                InputStream child_in = child.getInputStream();
                int c;
                while (true) {
                    if (!((c = child_in.read()) != -1)) break;
                    final int d = c;
                    Platform.runLater(() -> LogUtil.print(String.valueOf((char) d)));
                }
            } catch (IOException e) {
                LogUtil.warn("Running project failed");
                return;
            }
        }
    };

    @Override
    public boolean build() {
        Thread thread = new Thread(buildRunnable);
        thread.start();
        return true;
    }

    private String formatJavaSourceCode(String code) {
        try {
            String formattedCode = new Formatter().formatSource(code);
            return  formattedCode;
        } catch (FormatterException e) {
            LogUtil.warn("Format Java source code failed.");
            return code;
        }
    }

    @Override
    public boolean generate() {
        springBootProjectPath = PathUtil.getPath("EXPORT_PATH") + "/" + projectModelService.getProject().getArtifactId() + "-backend/";
        String resourcesPath = springBootProjectPath + "src/main/resources/";
        String sourcePath = springBootProjectPath + "src/main/java/" + (projectModelService.getProject().getGroupId() + "." + projectModelService.getProject().getArtifactId()).replace('.', '/') + "/";

        FileUtil.checkDirectory(resourcesPath);
        FileUtil.checkDirectory(sourcePath);

        FileUtil.write(springBootProjectPath + "pom.xml", stringifyPomXml());
        FileUtil.write(resourcesPath + "application.properties", stringifyApplicationProperties());
        FileUtil.write(sourcePath + StringUtil.capitalizedCamel(projectModelService.getProject().getArtifactId()) + "Application.java", stringifyApplicationEntry());
        FileUtil.write(springBootProjectPath + projectModelService.getProject().getArtifactId() + ".sql", stringifySql());

        String controllerPath = sourcePath + "controller/";
        String mybatisMapperPath = sourcePath + "mapper/";

        FileUtil.checkDirectory(controllerPath);
        FileUtil.checkDirectory(mybatisMapperPath);

        for (Table table : projectModelService.getProject().getDatabases().get(0).getTables()) {
            FileUtil.write(controllerPath + table.getCapitalizedCamelName() + "Controller.java", stringifyController(table));
            FileUtil.write(mybatisMapperPath + table.getCapitalizedCamelName() + "Mapper.java", stringifyMybatisMapper(table));
        }

        // Models
        String modelPath = sourcePath + "model/";
        FileUtil.checkDirectory(modelPath);
        for (Pair<String, String> modelPair : stringifyModels()) {
            FileUtil.write(modelPath + modelPair.getKey() + ".java", modelPair.getValue());
        }

        // Services
        String servicePath = sourcePath + "service/";
        FileUtil.checkDirectory(servicePath);
        for (Pair<String, String> servicePair : stringifyServices()) {
            FileUtil.write(servicePath + servicePair.getKey() + "Service.java", servicePair.getValue());
        }

        // ServiceImpl
        String serviceImplPath = sourcePath + "service/impl/";
        FileUtil.checkDirectory(serviceImplPath);
        for (Pair<String, String> serviceImplPair : stringifyServiceImpls()) {
            FileUtil.write(serviceImplPath + serviceImplPair.getKey() + "ServiceImpl.java", serviceImplPair.getValue());
        }

        // Config
        String configPath = sourcePath + "config/";
        FileUtil.checkDirectory(configPath);
        for (Plugin plugin: export.getPlugins()) {
            String configText = stringifyConfig(plugin);
            if (configText != null) {
                FileUtil.write(configPath + StringUtil.capitalizedCamel(plugin.getName()) +  "Config.java", configText);
            }
        }

        return true;
    }

    @Override
    public boolean run() {
        Thread thread = new Thread(runRunnable);
        thread.start();
        return true;
    }

    public String stringifyApplicationEntry() {
        Project project = projectModelService.getProject();
        Template template = new Template(EXPORT_TYPE, "Application.java");
        template.insert("groupId", project.getGroupId());
        template.insert("artifactId", project.getArtifactId());
        template.insert("ArtifactId", StringUtil.capitalize(project.getArtifactId()));
        return template.toString();
    }

    public String stringifyApplicationProperties() {
        String applicationPropertiesText = "";

        // Database properties
        applicationPropertiesText += "# database\n";
        if (export.getPlugin("mybatis") != null) {
            Database database = projectModelService.getProject().getDatabases().get(0);
            applicationPropertiesText += "spring.datasource.url = jdbc:" + database.getType() + "://" + database.getHost() + "\n";
            applicationPropertiesText += "spring.datasource.username = " + database.getUsername() + "\n";
            applicationPropertiesText += "spring.datasource.password = " + database.getPassword() + "\n\n";
        } else if (export.getPlugin("hibernate") != null) {
            LogUtil.warn("Hibernate have not be supported yet.");
        } else {
            LogUtil.warn("No Mybatis or Hibernate module found.");
        }

        return applicationPropertiesText;
    }

    public String stringifyConfig(Plugin plugin) {
        Project project = projectModelService.getProject();
        if (plugin.getName().equals("cross-origin")) {
            Template template = new Template(EXPORT_TYPE, "config/CrossOriginConfig.java");
            template.insert("groupId", project.getGroupId());
            template.insert("artifactId", project.getArtifactId());

            Config config = plugin.getConfig("allowedOrigins");
            if (config != null) {
                String allowedOriginsText = "";
                for (int i = 0; i < config.getValues().size(); i ++) {
                    allowedOriginsText += "\"" + config.getValues().get(i) + "\"";
                    if (i + 1 != config.getValues().size()) {
                        allowedOriginsText += ",";
                    }
                }
                template.insert("allowedOrigins", ".allowedOrigins(" + allowedOriginsText + ")");
            } else {
                template.remove("allowedOrigins");
            }

            config = plugin.getConfig("allowedHeaders");
            if (config != null) {
                String allowedHeadersText = "";
                for (int i = 0; i < config.getValues().size(); i ++) {
                    allowedHeadersText += "\"" + config.getValues().get(i) + "\"";
                    if (i + 1 != config.getValues().size()) {
                        allowedHeadersText += ",";
                    }
                }
                template.insert("allowedHeaders", ".allowedHeaders(" + allowedHeadersText + ")");
            } else {
                template.remove("allowedHeaders");
            }

            config = plugin.getConfig("allowedMethods");
            if (config != null) {
                String allowedMethodsText = "";
                for (int i = 0; i < config.getValues().size(); i ++) {
                    allowedMethodsText += "\"" + config.getValues().get(i) + "\"";
                    if (i + 1 != config.getValues().size()) {
                        allowedMethodsText += ",";
                    }
                }
                template.insert("allowedMethods", ".allowedMethods(" + allowedMethodsText + ")");
            } else {
                template.remove("allowedMethods");
            }

            return template.toString();
        }

        else if (plugin.getName().equals("swagger2") && plugin.isEnable()) {
            Template template = new Template(EXPORT_TYPE, "config/Swagger2Config.java");
            template.insert("groupId", project.getGroupId());
            template.insert("artifactId", project.getArtifactId());

            String title = project.getApiDocument().getTitle();
            if (title != null) {
                template.insert("title", title);
            } else if (project.getName() != null) {
                template.insert("title", project.getName());
            } else {
                template.insert("title", StringUtil.capitalize(project.getArtifactId()));
            }

            String description = project.getApiDocument().getDescription();
            if (description != null) {
                template.insert("description", description);
            } else {
                template.insert("description", "API Documents of " + project.getName());
            }

            template.insert("version", project.getVersion());

            String swaggerTags = "";
            List<Table> tables = project.getDatabases().get(0).getTables();
            for (int i = 0; i < tables.size(); i ++) {
                swaggerTags += "new Tag(\"" + tables.get(i).getCapitalizedCamelName() + "\", " + "\"" + tables.get(i).getDescription() + "\")";
                if (i + 1 == tables.size()) {
                    swaggerTags += "\n";
                } else {
                    swaggerTags += ",\n";
                }
            }
            template.insert("tags", swaggerTags);

            return template.toString();
        }
        return null;
    }

    public String stringifyController(Table table) {
        Project project = projectModelService.getProject();
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();

        Template template = new Template(EXPORT_TYPE, "controller/Controller.java");
        template.insert("groupId", groupId);
        template.insert("artifactId", artifactId);
        template.insert("Table", table.getCapitalizedCamelName());
        template.insert("table", table.getCamelName());
        template.insert("primaryKey", table.getCamelPrimaryKey());

        Plugin swagger2Plugin = export.getPlugin("swagger2");
        if (swagger2Plugin != null && swagger2Plugin.isEnable()) {
            ApiDocument apiDocument = project.getApiDocument();
            template.insert("swagger2Tag", "@Api(tags={\"" + table.getCapitalizedCamelName() + "\"})");
            template.insertOnce("swagger2ApiOperation", "@ApiOperation(value = \"" + apiDocument.getCreateKeyword() + table.getDescription() + "\")");
            template.insertOnce("swagger2ApiOperation", "@ApiOperation(value = \"" + apiDocument.getRetrieveKeyword() + table.getDescription() + "\")");
            template.insertOnce("swagger2ApiOperation", "@ApiOperation(value = \"" + apiDocument.getRetrieveListKeyword() + table.getDescription() + "\")");
            template.insertOnce("swagger2ApiOperation", "@ApiOperation(value = \"" + apiDocument.getUpdateKeyword() + table.getDescription() + "\")");
            template.insertOnce("swagger2ApiOperation", "@ApiOperation(value = \"" + apiDocument.getDeleteKeyword() + table.getDescription() + "\")");
        } else {
            template.remove("swagger2Tag");
            template.remove("swagger2ApiOperation");
        }

        return template.toString();
    }

    public List<Pair<String, String>> stringifyModels() {
        Project project = projectModelService.getProject();
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();
        List<Pair<String, String>> modelPairs = new ArrayList<>();
        for (Table table : project.getDatabases().get(0).getTables()) {
            JavaSourceFileModel modelFileModel = new JavaSourceFileModel();

            Bean bean = new Bean(AccessModifier.PUBLIC, table.getCapitalizedCamelName());
            for (Column column : table.getColumns()) {
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
        Template template = new Template("spring-boot-project", "model/RestResponse.java");
        template.insert("groupId", groupId);
        template.insert("artifactId", artifactId);
        modelPairs.add(new Pair<>("RestResponse", template.toString()));
        return modelPairs;
    }

    public String stringifyMybatisMapper(Table table) {
        Project project = projectModelService.getProject();
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();

        if (table.isPrimaryKeyBlank()) {
            LogUtil.warn("Mapper interface cannot be generated from a table without a primary key");
        }

        Template template = new Template(EXPORT_TYPE, "mapper/Mapper.java");
        template.insert("groupId", groupId);
        template.insert("artifactId", artifactId);
        template.insert("Table", table.getCapitalizedCamelName());
        template.insert("table", table.getCamelName());
        template.insert("primaryKey", table.getCamelPrimaryKey());
        template.insert("primary_key", table.getPrimaryKey());

        String columnKeys = "";
        String columnTags = "";
        String resultAnnotations = "";
        String columnKeyTagExpressions = "";

        for (int i = 0; i < table.getColumns().size(); i ++) {
            Column column = table.getColumns().get(i);
            if (!column.isPrimaryKey()) {
                columnKeys += column.getName();
                columnTags += "#{" + column.getCamelName() + "}";
                columnKeyTagExpressions += column.getName() + " = " + "#{" + column.getCamelName() + "}";
                if (i + 1 != table.getColumns().size()) {
                    columnKeys += ", ";
                    columnTags += ", ";
                    columnKeyTagExpressions += ", ";
                }
            }
            resultAnnotations += "@Result(property = \"" + column.getCamelName() + "\", column = \"" + column.getName() + "\")";
            if (i + 1 != table.getColumns().size()) {
                resultAnnotations += ",";
            }
            resultAnnotations += "\n";
        }

        template.insert("columnKeys", columnKeys);
        template.insert("columnTags", columnTags);
        template.insert("resultAnnotations", resultAnnotations);
        template.insert("columnKeyTagExpressions", columnKeyTagExpressions);

        return formatJavaSourceCode(template.toString());
    }

    public String stringifyPomXml() {
        if (export == null) {
            export = projectModelService.getProject().getSpringBootProjectExport();
        }

        PomXmlFileModel pomXmlFileModel = new PomXmlFileModel();
        Project project = projectModelService.getProject();
        pomXmlFileModel.setGroupId(project.getGroupId());
        pomXmlFileModel.setArtifactId(project.getArtifactId());
        pomXmlFileModel.setVersion(project.getVersion());

        List<Dependency> dependencies = new ArrayList<>();

        for (Plugin plugin : export.getPlugins()) {
            String pluginText = FileUtil.read(PathUtil.getProgramPath() + "exports/spring-boot-project/plugins/" + plugin.getName() + ".json");
            if (pluginText == null) {
                continue;
            }
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(pluginText);
                JsonNode dependenciesJsonNode = rootNode.path("dependencies");
                if (dependenciesJsonNode != null) {
                    if (dependenciesJsonNode.isArray()) {
                        for (JsonNode dependencyJsonNode : dependenciesJsonNode) {
                            Dependency dependency = new Dependency();
                            dependency.setGroupId(dependencyJsonNode.path("groupId").asText());
                            dependency.setArtifactId(dependencyJsonNode.path("artifactId").asText());
                            if (!dependencyJsonNode.path("version").asText().isEmpty()) {
                                dependency.setVersion(dependencyJsonNode.path("version").asText());
                            }
                            dependencies.add(dependency);
                        }
                    }
                }
            } catch (JsonProcessingException e) {
                LogUtil.warn(e.getMessage());
            }
        }
        pomXmlFileModel.setDependencies(dependencies);
        return pomXmlFileModel.toString();
    }

    public List<Pair<String, String>> stringifyServices() {
        Project project = projectModelService.getProject();
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();
        List<Pair<String, String>> servicePairs = new ArrayList<>();
        for (Table table : project.getDatabases().get(0).getTables()) {
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
        Project project = projectModelService.getProject();
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();
        List<Pair<String, String>> serviceImplPairs = new ArrayList<>();
        for (Table table : project.getDatabases().get(0).getTables()) {
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
        sqlFileModel.setTables(projectModelService.getProject().getDatabases().get(0).getTables());
        return sqlFileModel.toString();
    }
}
