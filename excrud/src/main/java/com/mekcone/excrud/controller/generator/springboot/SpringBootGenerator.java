package com.mekcone.excrud.controller.generator.springboot;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.type.TypeParameter;
import com.mekcone.excrud.Application;
import com.mekcone.excrud.constant.basic.DataType;
import com.mekcone.excrud.constant.basic.ExportType;
import com.mekcone.excrud.constant.properties.SpringBootBackendProperty;
import com.mekcone.excrud.controller.generator.BaseGenerator;
import com.mekcone.excrud.controller.generator.SqlGenerator;
import com.mekcone.excrud.controller.generator.springboot.extension.impl.Swagger2Extension;
import com.mekcone.excrud.controller.parser.PropertiesParser;
import com.mekcone.excrud.controller.parser.template.impl.JavaTemplate;
import com.mekcone.excrud.controller.parser.template.impl.UniversalTemplate;
import com.mekcone.excrud.model.project.export.impl.relationaldatabase.database.Column;
import com.mekcone.excrud.model.project.export.impl.relationaldatabase.database.Database;
import com.mekcone.excrud.model.project.export.impl.relationaldatabase.database.Table;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.springboot.Dependency;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class SpringBootGenerator extends BaseGenerator {

    // Bean types
    public final static String CONFIG_TYPE = "config";
    public final static String CONTROLLER_TYPE = "controller";
    public final static String ENTITY_TYPE = "entity";
    public final static String MAPPER_TYPE = "mapper";
    public final static String MODEL_TYPE = "model";
    public final static String SERVICE_TYPE = "service";
    public final static String SERVICE_IMPL_TYPE = "serviceImpl";

    // Method types
    public final static String CREATING_METHOD = "create";
    public final static String RETRIEVING_METHOD = "retrieve";
    public final static String RETRIEVING_LIST_METHOD = "retrieveList";
    public final static String UPDATING_METHOD = "update";
    public final static String DELETING_METHOD = "delete";

    public SpringBootGenerator(Project project) {
        initialize(project, ExportType.SPRING_BOOT);
    }

    public boolean build() {
        var file = new File(generatedDataPath);

        // Compiling
        try {
            // Only work on Windows
            Process child = Runtime.getRuntime().exec("mvn.cmd clean compile package", null, file);

            InputStream child_in = child.getInputStream();
            int c;
            while (true) {
                if (!((c = child_in.read()) != -1)) break;
                final int d = c;
                System.out.print(String.valueOf((char) d));
            }
        } catch (IOException e) {
            e.printStackTrace();
            //LogUtil.error("Compiling project failed");
            return false;
        }
        return true;
    }

    public void preprocessTemplate(UniversalTemplate universalTemplate, Table table) {
        String templateText = universalTemplate.toString();
        if (templateText == null || templateText.isEmpty()) {
            LogUtil.info("Preprocess templates failed");
            System.exit(-1);
        }
        if (templateText.contains("## groupId ##")) {
            universalTemplate.insert("groupId", project.getGroupId());
        }
        if (templateText.contains("## artifactId ##")) {
            universalTemplate.insert("artifactId", project.getArtifactId());
        }

        if (table == null) return;

        if (templateText.contains("## Table ##")) {
            universalTemplate.insert("Table", table.getUpperCamelCaseName());
        }

        if (templateText.contains("## table ##")) {
            universalTemplate.insert("table", table.getCamelCaseName());
        }

        if (templateText.contains("## primaryKey ##")) {
            universalTemplate.insert("primaryKey", table.getCamelCasePrimaryKey());
        }

        if (templateText.contains("## primary_key ##")) {
            universalTemplate.insert("primary_key", table.getPrimaryKey());
        }
    }

    private void preprocessTemplate(JavaTemplate javaTemplate, Table table) {
        String templateText = javaTemplate.toString();
        if (templateText.contains("__groupId__")) {
            javaTemplate.insert("groupId", project.getGroupId());
        }
        if (templateText.contains("__artifactId__")) {
            javaTemplate.insert("artifactId", project.getArtifactId());
        }

        if (table == null) return;

        if (templateText.contains("__Table__")) {
            javaTemplate.insert("Table", table.getUpperCamelCaseName());
        }

        if (templateText.contains("__table__")) {
            javaTemplate.insert("table", table.getCamelCaseName());
        }

        if (templateText.contains("__primaryKey__")) {
            javaTemplate.insert("primaryKey", table.getCamelCasePrimaryKey());
        }

        if (templateText.contains("__primary_key__")) {
            javaTemplate.insert("primary_key", table.getPrimaryKey());
        }
    }

    public void preprocessTemplate(UniversalTemplate universalTemplate) {
        preprocessTemplate(universalTemplate, null);
    }

    public void generate() {
        copyInitialTemplates();

        String resourcesPath = generatedDataPath + getPath("resourcesPath");
        String sourcePath = generatedDataPath + getPath("srcPath");

        FileUtil.write(generatedDataPath + "pom.xml", stringifyPomXml());
        FileUtil.write(resourcesPath + "application.properties", stringifyApplicationProperties());
        FileUtil.write(sourcePath + StrUtil.upperCamelCase(project.getArtifactId()) + "Application.java", stringifyApplicationEntry());

        String controllerPath = sourcePath + "controller/";
        String entityPath = sourcePath + "entity/";
        String mybatisMapperPath = sourcePath + "mapper/";
        String servicePath = sourcePath + "service/";
        String serviceImplPath = sourcePath + "service/impl/";

        for (var table : project.getExports().getDatabases().get(0).getTables()) {
            if (table.getCatalogueOf() != null && !table.getCatalogueOf().isEmpty()) {
                continue;
            }
            String tableName = table.getUpperCamelCaseName();
            addOutputFile(controllerPath + tableName + "Controller.java", CONTROLLER_TYPE, stringifyController(table));
            addOutputFile(entityPath + tableName + ".java", ENTITY_TYPE, stringifyEntity(table));
            addOutputFile(mybatisMapperPath + tableName + "Mapper.java", MAPPER_TYPE, stringifyMybatisMapper(table));
            addOutputFile(servicePath + tableName + "Service.java", SERVICE_TYPE, stringifyService(table));
            addOutputFile(serviceImplPath + tableName + "ServiceImpl.java", SERVICE_IMPL_TYPE, stringifyServiceImpl(table));
        }

        for (var outputFile: getOutputFiles()) {
            getCreatingMethod(outputFile);
        }

        // Config
        String configPath = sourcePath + "config/";

        // Cross origin
        if (oldExport.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_HEADERS) ||
                oldExport.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_METHODS) ||
                oldExport.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_ORIGINS)) {
            FileUtil.write(configPath + "CrossOriginConfig.java", stringifyConfig("cross_origin"));
        }

        // Swagger2
        if (oldExport.getBooleanProperty(SpringBootBackendProperty.SWAGGER2_ENABLE)) {
            FileUtil.write(configPath + "Swagger2Config.java", stringifyConfig("swagger2"));
        }

        // Utils
        String utilPath = sourcePath + "util/";
        FileUtil.write(utilPath + "ResultVOUtil.java", stringifyUtil("ResultVOUtil"));

        // VOs
        String VOPath = sourcePath + "VO/";
        FileUtil.write(VOPath + "ResultVO.java", stringifyVO("ResultVO"));

        // Execute extensions
        Swagger2Extension.execute(this);

        // Call write method of base generator
        write();

        LogUtil.info("Generate " + ExportType.SPRING_BOOT + " completed");
    }

    private List<String> getSupportedDependencies() {
        List<String> dependencies = new ArrayList<>();
        Class springBootBackendPropertyInterface = SpringBootBackendProperty.class;
        var fieldArray = springBootBackendPropertyInterface.getDeclaredFields();
        for (var field : fieldArray) {
            String fieldName = field.getName().toLowerCase();
            if (fieldName.contains("_enable")) {
                fieldName = fieldName.substring(0, fieldName.length() - "_enable".length());
                dependencies.add(fieldName);
            }
        }
        return dependencies;
    }


    public boolean run() {
        return false;
    }


    public String stringifyApplicationEntry() {
        var plainTemplate = new UniversalTemplate(templatePath + "Application.java");
        if (plainTemplate != null) {
            preprocessTemplate(plainTemplate);
        } else {
            LogUtil.info("Application.java not found");
        }
        plainTemplate.insert("ArtifactId", StrUtil.capitalize(project.getArtifactId()));
        return plainTemplate.toString();
    }


    public String stringifyApplicationProperties() {
        var globalPropertiesParser = new PropertiesParser();

        // Server port
        String serverPort = oldExport.getProperty(SpringBootBackendProperty.SERVER_PORT);
        if (serverPort != null && !serverPort.isEmpty()) {
            globalPropertiesParser.add("server.port", serverPort);
            globalPropertiesParser.addSeparator();
        }

        for (String dependencyName : getSupportedDependencies()) {
            if (oldExport.getBooleanProperty(dependencyName + "_enable")) {
                if (dependencyName.equals("mybatis")) {
                    Database defaultDatabase = project.getExports().getDatabases().get(0);
                    globalPropertiesParser.add("spring.datasource.url",
                            "jdbc:" + defaultDatabase.getType() + "://" + defaultDatabase.getHost() + "/" + defaultDatabase.getName());
                    globalPropertiesParser.add("spring.datasource.username", defaultDatabase.getUsername());
                    globalPropertiesParser.add("spring.datasource.password", defaultDatabase.getPassword());
                    globalPropertiesParser.addSeparator();
                } else {
                    var localPropertiesParser = PropertiesParser.readFrom(templatePath + "properties/" + dependencyName + ".properties");
                    if (localPropertiesParser != null) {
                        globalPropertiesParser.combine(localPropertiesParser);
                    }
                }
            }
        }
        return globalPropertiesParser.generate();
    }

    public String stringifyConfig(String configName) {
        switch (configName) {
            case "cross_origin":
                UniversalTemplate universalTemplate = new UniversalTemplate(templatePath + "/config/CrossOriginConfig.java");
                preprocessTemplate(universalTemplate);

                // Allowed headers
                if (oldExport.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_HEADERS)) {
                    String[] headers = oldExport.getProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_HEADERS).split(",");
                    if (headers.length >= 1) {
                        String allowedHeadersText = "";
                        for (int i = 0; i < headers.length; i++) {
                            allowedHeadersText += "\"" + headers[i].trim() + "\"";
                            if (i + 1 != headers.length) {
                                allowedHeadersText += ",";
                            }
                        }
                        universalTemplate.insert("allowedHeaders", ".allowedHeaders(" + allowedHeadersText + ")");
                    } else {
                        universalTemplate.remove("allowedHeaders");
                    }
                } else {
                    universalTemplate.remove("allowedHeaders");
                }

                // Allowed methods
                if (oldExport.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_METHODS)) {
                    String[] methods = oldExport.getProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_METHODS).split(",");
                    if (methods.length >= 1) {
                        String allowedMethodsText = "";
                        for (var i = 0; i < methods.length; i++) {
                            allowedMethodsText += "\"" + methods[i].trim() + "\"";
                            if (i + 1 != methods.length) {
                                allowedMethodsText += ",";
                            }
                        }
                        universalTemplate.insert("allowedMethods", ".allowedMethods(" + allowedMethodsText + ")");
                    } else {
                        universalTemplate.remove("allowedMethods");
                    }
                } else {
                    universalTemplate.remove("allowedMethods");
                }

                // Allowed origins
                if (oldExport.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_ORIGINS)) {
                    String[] origins = oldExport.getProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_ORIGINS).split(",");
                    if (origins.length >= 1) {
                        String allowedOriginsText = "";
                        for (var i = 0; i < origins.length; i++) {
                            allowedOriginsText += "\"" + origins[i].trim() + "\"";
                            if (i + 1 != origins.length) {
                                allowedOriginsText += ",";
                            }
                        }
                        universalTemplate.insert("allowedOrigins", ".allowedOrigins(" + allowedOriginsText + ")");
                    } else {
                        universalTemplate.remove("allowedOrigins");
                    }
                } else {
                    universalTemplate.remove("allowedOrigins");
                }

                return universalTemplate.toString();

            case "swagger2":
                universalTemplate = new UniversalTemplate(templatePath + "config/Swagger2Config.java");
                preprocessTemplate(universalTemplate);

                String title = project.getExports().getApiDocumentExport().getTitle();
                if (title != null) {
                    universalTemplate.insert("title", title.replace("{br}", ""));
                } else if (project.getName() != null) {
                    universalTemplate.insert("title", project.getName());
                } else {
                    universalTemplate.insert("title", StrUtil.capitalize(project.getArtifactId()));
                }

                String description = project.getExports().getApiDocumentExport().getDescription();
                if (description != null) {
                    universalTemplate.insert("description", description);
                } else {
                    universalTemplate.insert("description", "API Documents of " + project.getName());
                }

                universalTemplate.insert("version", project.getVersion());

                String swaggerTags = "";
                List<Table> tables = project.getExports().getDatabases().get(0).getTables();
                for (var i = 0; i < tables.size(); i++) {
                    swaggerTags += "new Tag(\"" + tables.get(i).getUpperCamelCaseName() + "\", " + "\"" + tables.get(i).getDescription() + "\")";
                    if (i + 1 == tables.size()) {
                        swaggerTags += "\n";
                    } else {
                        swaggerTags += ",\n";
                    }
                }
                universalTemplate.insert("tags", swaggerTags);

                return universalTemplate.toString();
            default:
                log.warn("Unsupported config item \"{}\"", configName);
                return null;
        }
    }

    public String stringifyController(Table table) {
        var javaTemplate = new JavaTemplate(templatePath + "controller/Controller.java");
        javaTemplate.preprocessForSpringBootProject(project, table);

        // Swagger2
        //Plugin swagger2Plugin = export.getPlugin(SpringBootProject.PLUGIN_SWAGGER2);
        List<MethodDeclaration> methods = javaTemplate.getCompilationUnit().getClassByName(
                table.getUpperCamelCaseName() + "Controller").get().getMethods();
        if (oldExport.getProperty("swagger2_enable").equals("true")) {
            var apiDocument = project.getExports().getApiDocumentExport();

            for (var methodDeclaration : methods) {
                var apiOperationAnnotationExpr = methodDeclaration.getAnnotationByName("ApiOperation").get();
                NodeList<MemberValuePair> pairs = apiOperationAnnotationExpr.asNormalAnnotationExpr().getPairs();
                for (var memberValuePair : pairs) {
                    if (memberValuePair.getNameAsString().equals("value")) {
                        memberValuePair.setValue(new NameExpr("\"" + apiDocument.getKeywordByType(methodDeclaration.getNameAsString()) + table.getDescription() + "\""));
                    }
                }
            }
        } else {
            NodeList<ImportDeclaration> imports = javaTemplate.getCompilationUnit().getImports();
            if (imports != null && !imports.isEmpty()) {
                for (var i = 0; i < imports.size(); i++) {
                    if (imports.get(i).getNameAsString().contains("io.swagger")) {
                        imports.get(i).remove();
                        i--;
                    }
                }
            }

            Optional<AnnotationExpr> apiAnnotation =
                    javaTemplate.getCompilationUnit().getClassByName(
                            table.getUpperCamelCaseName() + "Controller").get().getAnnotationByName("Api");
            if (apiAnnotation.get() != null) {
                apiAnnotation.get().remove();
            }

            for (var methodDeclaration : methods) {
                Optional<AnnotationExpr> apiOperationAnnotation = methodDeclaration.getAnnotationByName("ApiOperation");
                if (apiOperationAnnotation.get() != null) {
                    apiOperationAnnotation.get().remove();
                }
            }
        }

        // PageHelper
        String tableName = table.getUpperCamelCaseName();
        var retrieveListMethodDeclaration = new MethodDeclaration(NodeList.nodeList(Modifier.publicModifier()), new TypeParameter("ResultVO"), "retrieveList");
        retrieveListMethodDeclaration.addMarkerAnnotation("GetMapping");

        if (oldExport.getBooleanProperty(SpringBootBackendProperty.PAGE_HELPER_ENABLE)) {
            for (var methodDeclaration : javaTemplate.getCompilationUnit().getClassByName(tableName + "Controller").get().getMethods()) {
                if (methodDeclaration.getNameAsString().equals("retrieveList") && methodDeclaration.getParameters().isEmpty()) {
                    methodDeclaration.remove();
                }
            }
        } else {
            NodeList<ImportDeclaration> imports = javaTemplate.getCompilationUnit().getImports();
            if (imports != null && !imports.isEmpty()) {
                for (var i = 0; i < imports.size(); i++) {
                    if (imports.get(i).getNameAsString().contains("com.github.pagehelper")) {
                        imports.get(i).remove();
                        i--;
                    }
                }
            }

            for (var methodDeclaration : javaTemplate.getCompilationUnit().getClassByName(tableName + "Controller").get().getMethods()) {
                if (methodDeclaration.getNameAsString().equals("retrieveList") && !methodDeclaration.getParameters().isEmpty()) {
                    methodDeclaration.remove();
                }
            }
        }

        return javaTemplate.toString();
    }

    public String stringifyEntity(Table table) {
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();

        boolean lombokEnabled = oldExport.getBooleanProperty(SpringBootBackendProperty.LOMBOK_ENABLE);

        var entityCompilationUnit = new CompilationUnit();
        entityCompilationUnit.setPackageDeclaration(groupId + "." + artifactId + "." + "entity");
        ClassOrInterfaceDeclaration entityClassDeclaration =
                entityCompilationUnit.addClass(table.getUpperCamelCaseName(), Modifier.Keyword.PUBLIC);

        if (lombokEnabled) {
            entityCompilationUnit.addImport("lombok.Data");
            entityClassDeclaration.addMarkerAnnotation("Data");
        }
        for (var column : table.getColumns()) {
            String type = column.getType();
            if (!type.equals(DataType.JAVA_INT)) {
                type = DataType.JAVA_STRING;
            }

            entityClassDeclaration.addField(type, column.getCamelCaseName(table.getName()), Modifier.Keyword.PRIVATE);

            if (!lombokEnabled) {

                // Getter
                var getterMethodDeclaration =
                        entityClassDeclaration.addMethod("get" + column.getUpperCamelCaseName(table.getName()), Modifier.Keyword.PUBLIC);
                getterMethodDeclaration.setType(DataType.JAVA_STRING);
                BlockStmt getterMethodBody = new BlockStmt();
                getterMethodBody.addAndGetStatement(new ReturnStmt(column.getCamelCaseName(table.getName())));
                getterMethodDeclaration.setBody(getterMethodBody);

                // Setter
                var setterMethodDeclaration =
                        entityClassDeclaration.addMethod("set" + column.getUpperCamelCaseName(table.getName()), Modifier.Keyword.PUBLIC);
                setterMethodDeclaration.setType(DataType.JAVA_VOID);
                setterMethodDeclaration.addParameter(DataType.JAVA_STRING, column.getCamelCaseName(table.getName()));
                BlockStmt setterMethodBody = new BlockStmt();
                var assignExpr = new AssignExpr();
                assignExpr.setOperator(AssignExpr.Operator.ASSIGN);
                assignExpr.setTarget(new FieldAccessExpr(new NameExpr("this"), column.getCamelCaseName(table.getName())));
                assignExpr.setValue(new NameExpr(column.getCamelCaseName(table.getName())));
                setterMethodBody.addAndGetStatement(assignExpr);
                setterMethodDeclaration.setBody(setterMethodBody);
            }
        }
        return entityCompilationUnit.toString();
    }

    public String stringifyMybatisMapper(Table table) {
        if (!table.hasPrimaryKey()) {
            log.warn("Mapper interface cannot be generated from a table without a primary key");
        }

        var javaTemplate = new JavaTemplate(templatePath + "mapper/Mapper.java");
        javaTemplate.preprocessForSpringBootProject(project, table);

        for (var methodDeclaration : javaTemplate.getCompilationUnit().getInterfaceByName(table.getUpperCamelCaseName() + "Mapper").get().getMethods()) {
            NodeList<AnnotationExpr> annotations = methodDeclaration.getAnnotations();
            for (var annotationExpr : annotations) {
                if (annotationExpr.getNameAsString().equals("Insert")) {
                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
                            SqlGenerator.insertQuery(table, true)));
                } else if (annotationExpr.getNameAsString().equals("Results")) {
                    ArrayInitializerExpr array = new ArrayInitializerExpr();
                    for (Column column : table.getColumns()) {
                        if (methodDeclaration.getNameAsString().equals("retrieveList")) {
                            if (column.isDetail()) {
                                continue;
                            }
                        }
                        NormalAnnotationExpr resultAnnotation = new NormalAnnotationExpr();
                        resultAnnotation.setName("Result");
                        resultAnnotation.addPair("property", new StringLiteralExpr(column.getCamelCaseName(table.getName())));
                        resultAnnotation.addPair("column", new StringLiteralExpr(column.getName()));
                        array.getValues().add(resultAnnotation);
                    }
                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(array);
//                        YamlPrinter yamlPrinter = new YamlPrinter(true);
//                        LogUtil.error(-1, yamlPrinter.output(javaTemplate.getCompilationUnit()));
                } else if (annotationExpr.getNameAsString().equals("Update")) {
                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
                            SqlGenerator.updateQuery(table)));
                }
            }
        }

        return javaTemplate.toString();
    }

    public String stringifyPomXml() {
        // Root
        var rootElement = new Element("project");
        var document = new Document(rootElement);
        if (Application.getDescription() != null) {
            rootElement.addContent(new Comment(Application.getDescription()));
        }
        Namespace xmlns = Namespace.getNamespace("http://maven.apache.org/POM/4.0.0");
        rootElement.setNamespace(xmlns);
        Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.addNamespaceDeclaration(xsi);
        rootElement.setAttribute("schemaLocation", "http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd", xsi);
        rootElement.addContent(new Element("modelVersion", xmlns).setText("4.0.0"));

        // Parent
        var parentElement = new Element("parent", xmlns);
        rootElement.addContent(parentElement);
        parentElement.addContent(new Element("groupId", xmlns).setText("org.springframework.boot"));
        parentElement.addContent(new Element("artifactId", xmlns).setText("spring-boot-starter-parent"));
        parentElement.addContent(new Element("version", xmlns).setText("2.2.1.RELEASE"));
        parentElement.addContent(new Element("relativePath", xmlns));

        rootElement.addContent(new Element("groupId", xmlns).setText(project.getGroupId()));
        rootElement.addContent(new Element("artifactId", xmlns).setText(project.getArtifactId()));
        rootElement.addContent(new Element("version", xmlns).setText(project.getVersion()));
        rootElement.addContent(new Element("name", xmlns).setText(project.getArtifactId()));
        rootElement.addContent(new Element("description", xmlns).setText(project.getDescription()));

        // Properties
        var propertiesElement = new Element("properties", xmlns);
        rootElement.addContent(propertiesElement);
        propertiesElement.addContent(new Element("java.version", xmlns).setText("1.8"));

        // Dependencies
        List<Dependency> dependencies = new ArrayList<>();
        Class springBootBackendPropertyInterface = SpringBootBackendProperty.class;
        Field[] fields = springBootBackendPropertyInterface.getDeclaredFields();
        for (var field : fields) {
            String fieldName = field.getName().toLowerCase();
            if (fieldName.contains("_enable")) {
                if (oldExport.getBooleanProperty(fieldName)) {
                    fieldName = fieldName.substring(0, fieldName.length() - "_enable".length());
                    String pomDependenciesText = FileUtil.read(templatePath + "pom/" + fieldName + ".xml");
                    if (pomDependenciesText != null && !pomDependenciesText.isEmpty()) {
                        var xmlMapper = new XmlMapper();

                        try {
                            List<Dependency> pomDependencies = xmlMapper.readValue(pomDependenciesText, new TypeReference<List<Dependency>>() {
                            });
                            if (pomDependencies != null && !pomDependencies.isEmpty()) {
                                dependencies.addAll(pomDependencies);
                            }
                        } catch (JsonProcessingException e) {
                            log.error(e.getMessage());
                        }
                    }
                } else {
                    log.warn("\"{}\" not found", fieldName);
                }
            }
        }

        var dependenciesElement = new Element("dependencies", xmlns);
        rootElement.addContent(dependenciesElement);

        for (var dependency : dependencies) {
            var dependencyElement = new Element("dependency", xmlns);
            dependencyElement.addContent(new Element("groupId", xmlns).setText(dependency.getGroupId()));
            dependencyElement.addContent(new Element("artifactId", xmlns).setText(dependency.getArtifactId()));
            if (dependency.getVersion() != null) {
                dependencyElement.addContent(new Element("version", xmlns).setText(dependency.getVersion()));
            }
            if (dependency.getScope() != null) {
                dependencyElement.addContent(new Element("scope", xmlns).setText(dependency.getScope()));
            }

            List<Dependency> exclusionDependencies = dependency.getExclusions();
            if (exclusionDependencies != null && !exclusionDependencies.isEmpty()) {
                var exclusionsElement = new Element("exclusions", xmlns);
                dependencyElement.addContent(exclusionsElement);
                for (var exclusionDependency : dependency.getExclusions()) {
                    var exclusionElement = new Element("exclusion", xmlns);
                    exclusionElement.addContent(new Element("groupId", xmlns).setText(exclusionDependency.getGroupId()));
                    exclusionElement.addContent(new Element("artifactId", xmlns).setText(exclusionDependency.getArtifactId()));
                    exclusionsElement.addContent(exclusionElement);
                }
            }
            dependenciesElement.addContent(dependencyElement);
        }

        // <build></build>
        var buildElement = new Element("build", xmlns);
        rootElement.addContent(buildElement);
        var pluginsElement = new Element("plugins", xmlns);
        buildElement.addContent(pluginsElement);
        var pluginElement = new Element("plugin", xmlns);
        pluginsElement.addContent(pluginElement);
        pluginElement.addContent(new Element("groupId", xmlns).setText("org.springframework.boot"));
        pluginElement.addContent(new Element("artifactId", xmlns).setText("spring-boot-maven-plugin"));

        // Generate string
        var format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        var xmlOutputter = new XMLOutputter(format);
        var byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            xmlOutputter.output(document, byteArrayOutputStream);
        } catch (IOException e) {
            log.warn(e.getMessage());
        }
        return byteArrayOutputStream.toString();
    }

    public String stringifyService(Table table) {
        var javaTemplate = new JavaTemplate(templatePath + "service/Service.java");
        javaTemplate.preprocessForSpringBootProject(project, table);

        if (!oldExport.getBooleanProperty(SpringBootBackendProperty.PAGE_HELPER_ENABLE)) {
            javaTemplate.removeImport("com.github.pagehelper");
            for (var methodDeclaration : javaTemplate.getCompilationUnit().getInterfaceByName(
                    table.getUpperCamelCaseName() + "Service").get().getMethods()) {
                if (methodDeclaration.getNameAsString().equals("retrieveList") && !methodDeclaration.getParameters().isEmpty()) {
                    methodDeclaration.remove();
                    break;
                }
            }
        }

        return javaTemplate.toString();
    }

    public String stringifyServiceImpl(Table table) {
        var javaTemplate = new JavaTemplate(templatePath + "service/impl/ServiceImpl.java");
        javaTemplate.preprocessForSpringBootProject(project, table);

        if (oldExport.getBooleanProperty(SpringBootBackendProperty.PAGE_HELPER_ENABLE)) {

        } else {
            javaTemplate.removeImport("com.github.pagehelper");
            try {
                List<MethodDeclaration> methods =
                        javaTemplate.getCompilationUnit().getClassByName(table.getUpperCamelCaseName() + "ServiceImpl").get().getMethods();
                for (var methodDeclaration : methods) {
                    if (methodDeclaration.getNameAsString().equals("retrieveList") && !methodDeclaration.getParameters().isEmpty()) {
                        methodDeclaration.remove();
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }

        }

        return javaTemplate.toString();
    }

    public String stringifyUtil(String utilName) {
        var javaTemplate = new JavaTemplate(templatePath + "util/" + utilName + ".java");
        javaTemplate.preprocessForSpringBootProject(project, null);
        return javaTemplate.toString();
    }

    public String stringifyVO(String VOName) {
        var javaTemplate = new JavaTemplate(templatePath + "VO/" + VOName + ".java");
        javaTemplate.preprocessForSpringBootProject(project, null);
        return javaTemplate.toString();
    }


    // Process output files

    private List<BaseGenerator.OutputFile> getBeans(String type) {
        List<BaseGenerator.OutputFile> outputFiles = new ArrayList<>();
        for (var outputFile: getOutputFiles()) {
            if (outputFile.getType().equals(type)) {
                outputFiles.add(outputFile);
            }
        }
        return outputFiles;
    }

    public List<BaseGenerator.OutputFile> getControllers() {
        return getBeans(SpringBootGenerator.CONTROLLER_TYPE);
    }

    public List<BaseGenerator.OutputFile> getEntities() {
        return getBeans(SpringBootGenerator.ENTITY_TYPE);
    }

    public List<BaseGenerator.OutputFile> getServices() {
        return getBeans(SpringBootGenerator.SERVICE_TYPE);
    }

    public List<BaseGenerator.OutputFile> getServiceImpls() {
        return getBeans(SpringBootGenerator.SERVICE_IMPL_TYPE);
    }

    public MethodDeclaration getMethodByType(String type, BaseGenerator.OutputFile outputFile) {
        CompilationUnit compilationUnit = StaticJavaParser.parse(outputFile.getContent());
        System.out.println(outputFile.getFileName());
        //ClassOrInterfaceDeclaration mainClass = compilationUnit.getClassByName(outputFile.getFileName()).get();
        //System.out.println(mainClass.getNameAsString());
        return null;
    }

    public MethodDeclaration getCreatingMethod(BaseGenerator.OutputFile outputFile) {
        return getMethodByType(SpringBootGenerator.CREATING_METHOD, outputFile);
    }

    public MethodDeclaration getRetrievingMethod(BaseGenerator.OutputFile outputFile) {
        return getMethodByType(SpringBootGenerator.RETRIEVING_METHOD, outputFile);
    }

    public MethodDeclaration getRetrievingListMethod(BaseGenerator.OutputFile outputFile) {
        return getMethodByType(SpringBootGenerator.RETRIEVING_LIST_METHOD, outputFile);
    }

    public MethodDeclaration getUpdatingMethod(BaseGenerator.OutputFile outputFile) {
        return getMethodByType(SpringBootGenerator.UPDATING_METHOD, outputFile);
    }

    public MethodDeclaration getDeletingMethod(BaseGenerator.OutputFile outputFile) {
        return getMethodByType(SpringBootGenerator.DELETING_METHOD, outputFile);
    }
}
