package com.mekcone.excrud.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
import com.mekcone.excrud.constant.DataType;
import com.mekcone.excrud.constant.ExportType;
import com.mekcone.excrud.constant.SpringBootBackendProperty;
import com.mekcone.excrud.enums.ErrorEnum;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.apidoc.ApiDocument;
import com.mekcone.excrud.model.springboot.Dependency;
import com.mekcone.excrud.model.project.Export;
import com.mekcone.excrud.model.database.Column;
import com.mekcone.excrud.model.database.Database;
import com.mekcone.excrud.model.database.Table;
import com.mekcone.excrud.model.template.JavaTemplate;
import com.mekcone.excrud.model.template.PlainTemplate;
import com.mekcone.excrud.parser.PropertiesParser;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.StringUtil;
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

public class SpringBootBackendGenerator {

    private final String EXCRUD_HOME = Application.getHomeDirectory();
    private Export export;
    private Project project;
    private String springBootBackendPath;
    private final String templatePath = EXCRUD_HOME + "/exports/" + ExportType.SPRING_BOOT_BACKEND + "/templates/";


    public SpringBootBackendGenerator(Project project, Export export) {
        this.project = project;
        this.export = export;
        springBootBackendPath = ExportType.SPRING_BOOT_BACKEND + "/";
    }

    public boolean build() {
        File file = new File(springBootBackendPath);

        // Compiling
        try {
            // Only work on Windows
            Process child = Runtime.getRuntime().exec("mvn.cmd clean compile package", null, file);

            InputStream child_in = child.getInputStream();
            int c;
            while (true) {
                if (!((c = child_in.read()) != -1)) break;
                final int d = c;
                LogUtil.print(String.valueOf((char) d));
            }
        } catch (IOException e) {
            LogUtil.error(-1, "Compiling project failed");
            return false;
        }
        return true;
    }

    private void createDirectories() {
        if (EXCRUD_HOME == null) {
            LogUtil.error(ErrorEnum.EXCRUD_HOME_ENV_VARIABLE_NOT_SET);
        }

        List<String> paths = FileUtil.readLine(EXCRUD_HOME + "/exports/" + ExportType.SPRING_BOOT_BACKEND + "/directories.txt");
        if (paths == null) {
            LogUtil.error(ErrorEnum.SBP_DIRECTORIES_TXT_NOT_FOUND);
        }
        String sourcePath = ExportType.SPRING_BOOT_BACKEND + "/src/main/java/" + (project.getGroupId() + "." +
                project.getArtifactId()).replace('.', '/');
        for (String path : paths) {
            path = path.replace("SRC_PATH", sourcePath);
            FileUtil.checkDirectory(path);
        }
    }

    private void preprocessTemplate(PlainTemplate plainTemplate, Table table) {
        String templateText = plainTemplate.toString();
        if (templateText.indexOf("## groupId ##") > -1) {
            plainTemplate.insert("groupId", project.getGroupId());
        }
        if (templateText.indexOf("## artifactId ##") > -1) {
            plainTemplate.insert("artifactId", project.getArtifactId());
        }

        if (table == null) return;

        if (templateText.indexOf("## Table ##") > -1) {
            plainTemplate.insert("Table", table.getCapitalizedCamelName());
        }

        if (templateText.indexOf("## table ##") > -1) {
            plainTemplate.insert("table", table.getCamelName());
        }

        if (templateText.indexOf("## primaryKey ##") > -1) {
            plainTemplate.insert("primaryKey", table.getCamelPrimaryKey());
        }

        if (templateText.indexOf("## primary_key ##") > -1) {
            plainTemplate.insert("primary_key", table.getPrimaryKey());
        }
    }

    private void preprocessTemplate(JavaTemplate javaTemplate, Table table) {
        String templateText = javaTemplate.toString();
        if (templateText.indexOf("__groupId__") > -1) {
            javaTemplate.insert("groupId", project.getGroupId());
        }
        if (templateText.indexOf("__artifactId__") > -1) {
            javaTemplate.insert("artifactId", project.getArtifactId());
        }

        if (table == null) return;

        if (templateText.indexOf("__Table__") > -1) {
            javaTemplate.insert("Table", table.getCapitalizedCamelName());
        }

        if (templateText.indexOf("__table__") > -1) {
            javaTemplate.insert("table", table.getCamelName());
        }

        if (templateText.indexOf("__primaryKey__") > -1) {
            javaTemplate.insert("primaryKey", table.getCamelPrimaryKey());
        }

        if (templateText.indexOf("__primary_key__") > -1) {
            javaTemplate.insert("primary_key", table.getPrimaryKey());
        }
    }

    private void preprocessTemplate(PlainTemplate plainTemplate) {
        preprocessTemplate(plainTemplate, null);
    }


    public void generate() {
        createDirectories();
        String resourcesPath = ExportType.SPRING_BOOT_BACKEND + "/src/main/resources/";
        String sourcePath = springBootBackendPath + "src/main/java/" + (project.getGroupId() + "." + project.getArtifactId()).replace('.', '/') + "/";

        FileUtil.checkDirectory(resourcesPath);

        FileUtil.write(springBootBackendPath + "pom.xml", stringifyPomXml());
        FileUtil.write(resourcesPath + "application.properties", stringifyApplicationProperties());
        FileUtil.write(sourcePath + StringUtil.capitalizedCamel(project.getArtifactId()) + "Application.java", stringifyApplicationEntry());

        String controllerPath = sourcePath + "controller/";
        String entityPath = sourcePath + "entity/";
        String mybatisMapperPath = sourcePath + "mapper/";
        String servicePath = sourcePath + "service/";
        String serviceImplPath = sourcePath + "service/impl/";

        for (Table table : project.getDatabases().get(0).getTables()) {
            FileUtil.write(controllerPath + table.getCapitalizedCamelName() + "Controller.java", stringifyController(table));
            FileUtil.write(entityPath + table.getCapitalizedCamelName() + ".java", stringifyEntity(table));
            FileUtil.write(mybatisMapperPath + table.getCapitalizedCamelName() + "Mapper.java", stringifyMybatisMapper(table));
            FileUtil.write(servicePath + table.getCapitalizedCamelName() + "Service.java", stringifyService(table));
            FileUtil.write(serviceImplPath + table.getCapitalizedCamelName() + "ServiceImpl.java", stringifyServiceImpl(table));
        }

        // Config
        String configPath = sourcePath + "config/";

        // Cross origin
        if (export.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_HEADERS) ||
                export.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_METHODS) ||
                export.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_ORIGINS)) {
            FileUtil.write(configPath + "CrossOriginConfig.java", stringifyConfig("cross_origin"));
        }

        // Swagger2
        if (export.getBooleanProperty(SpringBootBackendProperty.SWAGGER2_ENABLE)) {
            FileUtil.write(configPath + "Swagger2Config.java", stringifyConfig("swagger2"));
        }

        // Utils
        String utilPath = sourcePath + "util/";
        FileUtil.write(utilPath + "ResultVOUtil.java", stringifyUtil("ResultVOUtil"));

        // VOs
        String VOPath = sourcePath + "VO/";
        FileUtil.write(VOPath + "ResultVO.java", stringifyVO("ResultVO"));

        LogUtil.info("Generate " + ExportType.SPRING_BOOT_BACKEND + " completed");

        // throw new ExportException(ExportExceptionEnums.GENERATE_PROJECT_FAILED);
    }

    private List<String> getSupportedDependencies() {
        List<String> dependencies = new ArrayList<>();
        Class springBootBackendPropertyInterface = SpringBootBackendProperty.class;
        Field[] fields = springBootBackendPropertyInterface.getDeclaredFields();
        for (Field field : fields) {
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
        PlainTemplate plainTemplate = new PlainTemplate(templatePath + "Application.java");
        preprocessTemplate(plainTemplate);
        plainTemplate.insert("ArtifactId", StringUtil.capitalize(project.getArtifactId()));
        return plainTemplate.toString();
    }


    public String stringifyApplicationProperties() {
        PropertiesParser globalPropertiesParser = new PropertiesParser();

        // Server port
        String serverPort = export.getProperty(SpringBootBackendProperty.SERVER_PORT);
        if (serverPort != null && !serverPort.isEmpty()) {
            globalPropertiesParser.add("server.port", serverPort);
            globalPropertiesParser.addSeparator();
        }

        for (String dependencyName : getSupportedDependencies()) {
            if (export.getBooleanProperty(dependencyName + "_enable")) {
                if (dependencyName.equals("mybatis")) {
                    Database defaultDatabase = project.getDatabases().get(0);
                    globalPropertiesParser.add("spring.datasource.url",
                            "jdbc:" + defaultDatabase.getType() + "://" + defaultDatabase.getHost() + "/" + defaultDatabase.getName());
                    globalPropertiesParser.add("spring.datasource.username", defaultDatabase.getUsername());
                    globalPropertiesParser.add("spring.datasource.password", defaultDatabase.getPassword());
                    globalPropertiesParser.addSeparator();
                } else {
                    PropertiesParser localPropertiesParser = PropertiesParser.readFrom(templatePath + "properties/" + dependencyName + ".properties");
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
                PlainTemplate plainTemplate = new PlainTemplate(templatePath + "/config/CrossOriginConfig.java");
                preprocessTemplate(plainTemplate);

                // Allowed headers
                if (export.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_HEADERS)) {
                    String[] headers = export.getProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_HEADERS).split(",");
                    if (headers.length >= 1) {
                        String allowedHeadersText = "";
                        for (int i = 0; i < headers.length; i++) {
                            allowedHeadersText += "\"" + headers[i].trim() + "\"";
                            if (i + 1 != headers.length) {
                                allowedHeadersText += ",";
                            }
                        }
                        plainTemplate.insert("allowedHeaders", ".allowedHeaders(" + allowedHeadersText + ")");
                    } else {
                        plainTemplate.remove("allowedHeaders");
                    }
                } else {
                    plainTemplate.remove("allowedHeaders");
                }

                // Allowed methods
                if (export.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_METHODS)) {
                    String[] methods = export.getProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_METHODS).split(",");
                    if (methods.length >= 1) {
                        String allowedMethodsText = "";
                        for (int i = 0; i < methods.length; i++) {
                            allowedMethodsText += "\"" + methods[i].trim() + "\"";
                            if (i + 1 != methods.length) {
                                allowedMethodsText += ",";
                            }
                        }
                        plainTemplate.insert("allowedMethods", ".allowedMethods(" + allowedMethodsText + ")");
                    } else {
                        plainTemplate.remove("allowedMethods");
                    }
                } else {
                    plainTemplate.remove("allowedMethods");
                }

                // Allowed origins
                if (export.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_ORIGINS)) {
                    String[] origins = export.getProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_ORIGINS).split(",");
                    if (origins.length >= 1) {
                        String allowedOriginsText = "";
                        for (int i = 0; i < origins.length; i++) {
                            allowedOriginsText += "\"" + origins[i].trim() + "\"";
                            if (i + 1 != origins.length) {
                                allowedOriginsText += ",";
                            }
                        }
                        plainTemplate.insert("allowedOrigins", ".allowedOrigins(" + allowedOriginsText + ")");
                    } else {
                        plainTemplate.remove("allowedOrigins");
                    }
                } else {
                    plainTemplate.remove("allowedOrigins");
                }

                return plainTemplate.toString();

            case "swagger2":
                plainTemplate = new PlainTemplate(templatePath + "config/Swagger2Config.java");
                preprocessTemplate(plainTemplate);

                String title = project.getApiDocument().getTitle();
                if (title != null) {
                    plainTemplate.insert("title", title.replace("{br}", ""));
                } else if (project.getName() != null) {
                    plainTemplate.insert("title", project.getName());
                } else {
                    plainTemplate.insert("title", StringUtil.capitalize(project.getArtifactId()));
                }

                String description = project.getApiDocument().getDescription();
                if (description != null) {
                    plainTemplate.insert("description", description);
                } else {
                    plainTemplate.insert("description", "API Documents of " + project.getName());
                }

                plainTemplate.insert("version", project.getVersion());

                String swaggerTags = "";
                List<Table> tables = project.getDatabases().get(0).getTables();
                for (int i = 0; i < tables.size(); i++) {
                    swaggerTags += "new Tag(\"" + tables.get(i).getCapitalizedCamelName() + "\", " + "\"" + tables.get(i).getDescription() + "\")";
                    if (i + 1 == tables.size()) {
                        swaggerTags += "\n";
                    } else {
                        swaggerTags += ",\n";
                    }
                }
                plainTemplate.insert("tags", swaggerTags);

                return plainTemplate.toString();
            default:
                LogUtil.warn("Unsupported config item \"" + configName + "\"");
                return null;
        }
        /**/
    }

    public String stringifyController(Table table) {
        JavaTemplate javaTemplate = new JavaTemplate(templatePath + "controller/Controller.java");
        javaTemplate.preprocessForSpringBootProject(project, table);

        // Swagger2
        //Plugin swagger2Plugin = export.getPlugin(SpringBootProject.PLUGIN_SWAGGER2);
        List<MethodDeclaration> methods = javaTemplate.getCompilationUnit().getClassByName(
                table.getCapitalizedCamelName() + "Controller").get().getMethods();
        if (export.getProperty("swagger2_enable").equals("true")) {
            ApiDocument apiDocument = project.getApiDocument();

            for (MethodDeclaration method : methods) {
                AnnotationExpr apiOperationAnnotation = method.getAnnotationByName("ApiOperation").get();
                NodeList<MemberValuePair> pairs = apiOperationAnnotation.asNormalAnnotationExpr().getPairs();
                for (MemberValuePair memberValuePair : pairs) {
                    if (memberValuePair.getNameAsString().equals("value")) {
                        memberValuePair.setValue(new NameExpr("\"" + apiDocument.getKeywordByType(method.getNameAsString()) + table.getDescription() + "\""));
                    }
                }
            }
        } else {
            NodeList<ImportDeclaration> imports = javaTemplate.getCompilationUnit().getImports();
            if (imports != null && !imports.isEmpty()) {
                for (int i = 0; i < imports.size(); i++) {
                    if (imports.get(i).getNameAsString().contains("io.swagger")) {
                        imports.get(i).remove();
                        i--;
                    }
                }
            }

            Optional<AnnotationExpr> apiAnnotation =
                    javaTemplate.getCompilationUnit().getClassByName(
                            table.getCapitalizedCamelName() + "Controller").get().getAnnotationByName("Api");
            if (apiAnnotation.get() != null) {
                apiAnnotation.get().remove();
            }

            for (MethodDeclaration method : methods) {
                Optional<AnnotationExpr> apiOperationAnnotation = method.getAnnotationByName("ApiOperation");
                if (apiOperationAnnotation.get() != null) {
                    apiOperationAnnotation.get().remove();
                }
            }
        }

        // PageHelper
        //Plugin pageHelperPlugin = export.getPlugin(SpringBootProject.PLUGIN_PAGE_HELPER);
        String tableName = table.getCapitalizedCamelName();
        MethodDeclaration retrieveListMethod = new MethodDeclaration(NodeList.nodeList(Modifier.publicModifier()), new TypeParameter("ResultVO"), "retrieveList");
        retrieveListMethod.addMarkerAnnotation("GetMapping");

        if (export.getBooleanProperty(SpringBootBackendProperty.PAGE_HELPER_ENABLE)) {
            for (MethodDeclaration method : javaTemplate.getCompilationUnit().getClassByName(tableName + "Controller").get().getMethods()) {
                if (method.getNameAsString().equals("retrieveList") && method.getParameters().isEmpty()) {
                    method.remove();
                }
            }
        } else {
            NodeList<ImportDeclaration> imports = javaTemplate.getCompilationUnit().getImports();
            if (imports != null && !imports.isEmpty()) {
                for (int i = 0; i < imports.size(); i++) {
                    if (imports.get(i).getNameAsString().contains("com.github.pagehelper")) {
                        imports.get(i).remove();
                        i--;
                    }
                }
            }

            for (MethodDeclaration method : javaTemplate.getCompilationUnit().getClassByName(tableName + "Controller").get().getMethods()) {
                if (method.getNameAsString().equals("retrieveList") && !method.getParameters().isEmpty()) {
                    method.remove();
                }
            }
        }

        return javaTemplate.toString();
    }

    public String stringifyEntity(Table table) {
        String groupId = project.getGroupId();
        String artifactId = project.getArtifactId();

        boolean lombokEnabled = export.getBooleanProperty(SpringBootBackendProperty.LOMBOK_ENABLE);

        CompilationUnit entityCompilationUnit = new CompilationUnit();
        entityCompilationUnit.setPackageDeclaration(groupId + "." + artifactId + "." + "entity");
        ClassOrInterfaceDeclaration entityClassDeclaration =
                entityCompilationUnit.addClass(table.getCapitalizedCamelName(), Modifier.Keyword.PUBLIC);

        if (lombokEnabled) {
            entityCompilationUnit.addImport("lombok.Data");
            entityClassDeclaration.addMarkerAnnotation("Data");
        }
        for (Column column : table.getColumns()) {
            String type = column.getType();
            if (!type.equals(DataType.JAVA_INT)) {
                type = DataType.JAVA_STRING;
            }

            entityClassDeclaration.addField(type, column.getCamelName(table.getName()), Modifier.Keyword.PRIVATE);

            if (!lombokEnabled) {

                // Getter
                MethodDeclaration getterMethodDeclaration =
                        entityClassDeclaration.addMethod("get" + column.getCapitalizedCamelName(table.getName()), Modifier.Keyword.PUBLIC);
                getterMethodDeclaration.setType(DataType.JAVA_STRING);
                BlockStmt getterMethodBody = new BlockStmt();
                getterMethodBody.addAndGetStatement(new ReturnStmt(column.getCamelName(table.getName())));
                getterMethodDeclaration.setBody(getterMethodBody);

                // Setter
                MethodDeclaration setterMethodDeclaration =
                        entityClassDeclaration.addMethod("set" + column.getCapitalizedCamelName(table.getName()), Modifier.Keyword.PUBLIC);
                setterMethodDeclaration.setType(DataType.JAVA_VOID);
                setterMethodDeclaration.addParameter(DataType.JAVA_STRING, column.getCamelName(table.getName()));
                BlockStmt setterMethodBody = new BlockStmt();
                AssignExpr assignExpr = new AssignExpr();
                assignExpr.setOperator(AssignExpr.Operator.ASSIGN);
                assignExpr.setTarget(new FieldAccessExpr(new NameExpr("this"), column.getCamelName(table.getName())));
                assignExpr.setValue(new NameExpr(column.getCamelName(table.getName())));
                setterMethodBody.addAndGetStatement(assignExpr);
                setterMethodDeclaration.setBody(setterMethodBody);
            }
        }
        return entityCompilationUnit.toString();
    }

    public String stringifyMybatisMapper(Table table) {
        if (table.isPrimaryKeyBlank()) {
            LogUtil.warn("Mapper interface cannot be generated from a table without a primary key");
        }

        JavaTemplate javaTemplate = new JavaTemplate(templatePath + "mapper/Mapper.java");
        javaTemplate.preprocessForSpringBootProject(project, table);

        for (MethodDeclaration method : javaTemplate.getCompilationUnit().getInterfaceByName(table.getCapitalizedCamelName() + "Mapper").get().getMethods()) {
            NodeList<AnnotationExpr> annotations = method.getAnnotations();
            for (AnnotationExpr annotation : annotations) {
                if (annotation.getNameAsString().equals("Insert")) {
                    annotation.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
                            SqlGenerator.insertQuery(table, true)));
                } else if (annotation.getNameAsString().equals("Results")) {
                    ArrayInitializerExpr array = new ArrayInitializerExpr();
                    for (Column column : table.getColumns()) {
                        if (method.getNameAsString().equals("retrieveList")) {
                            if (column.isDetail()) {
                                continue;
                            }
                        }
                        NormalAnnotationExpr resultAnnotation = new NormalAnnotationExpr();
                        resultAnnotation.setName("Result");
                        resultAnnotation.addPair("property", new StringLiteralExpr(column.getCamelName(table.getName())));
                        resultAnnotation.addPair("column", new StringLiteralExpr(column.getName()));
                        array.getValues().add(resultAnnotation);
                    }
                    annotation.asSingleMemberAnnotationExpr().setMemberValue(array);
//                        YamlPrinter yamlPrinter = new YamlPrinter(true);
//                        LogUtil.error(-1, yamlPrinter.output(javaTemplate.getCompilationUnit()));
                } else if (annotation.getNameAsString().equals("Update")) {
                    annotation.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
                            SqlGenerator.updateQuery(table)));
                }
            }
        }

        return javaTemplate.toString();
    }

    public String stringifyPomXml() {
        // Root
        Element rootElement = new Element("project");
        Document document = new Document(rootElement);
        if (Application.description != null) {
            rootElement.addContent(new Comment(Application.description));
        }
        Namespace xmlns = Namespace.getNamespace("http://maven.apache.org/POM/4.0.0");
        rootElement.setNamespace(xmlns);
        Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.addNamespaceDeclaration(xsi);
        rootElement.setAttribute("schemaLocation", "http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd", xsi);
        rootElement.addContent(new Element("modelVersion", xmlns).setText("4.0.0"));

        // Parent
        Element parentElement = new Element("parent", xmlns);
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
        Element propertiesElement = new Element("properties", xmlns);
        rootElement.addContent(propertiesElement);
        propertiesElement.addContent(new Element("java.version", xmlns).setText("1.8"));

        // Dependencies
        List<Dependency> dependencies = new ArrayList<>();
        Class springBootBackendPropertyInterface = SpringBootBackendProperty.class;
        Field[] fields = springBootBackendPropertyInterface.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName().toLowerCase();
            if (fieldName.contains("_enable")) {
                if (export.getBooleanProperty(fieldName)) {
                    fieldName = fieldName.substring(0, fieldName.length() - "_enable".length());
                    String pomDependenciesText = FileUtil.read(templatePath + "pom/" + fieldName + ".xml");
                    if (pomDependenciesText != null && !pomDependenciesText.isEmpty()) {
                        XmlMapper xmlMapper = new XmlMapper();

                        try {
                            List<Dependency> pomDependencies = xmlMapper.readValue(pomDependenciesText, new TypeReference<List<Dependency>>() {
                            });
                            if (pomDependencies != null && !pomDependencies.isEmpty()) {
                                dependencies.addAll(pomDependencies);
                            }
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    LogUtil.warn("\"" + fieldName + "\" not found");
                }
            }
        }

        Element dependenciesElement = new Element("dependencies", xmlns);
        rootElement.addContent(dependenciesElement);

        for (Dependency dependency : dependencies) {
            Element dependencyElement = new Element("dependency", xmlns);
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
                Element exclusionsElement = new Element("exclusions", xmlns);
                dependencyElement.addContent(exclusionsElement);
                for (Dependency exclusionDependency : dependency.getExclusions()) {
                    Element exclusionElement = new Element("exclusion", xmlns);
                    exclusionElement.addContent(new Element("groupId", xmlns).setText(exclusionDependency.getGroupId()));
                    exclusionElement.addContent(new Element("artifactId", xmlns).setText(exclusionDependency.getArtifactId()));
                    exclusionsElement.addContent(exclusionElement);
                }
            }
            dependenciesElement.addContent(dependencyElement);
        }

        // <build></build>
        Element buildElement = new Element("build", xmlns);
        rootElement.addContent(buildElement);
        Element pluginsElement = new Element("plugins", xmlns);
        buildElement.addContent(pluginsElement);
        Element pluginElement = new Element("plugin", xmlns);
        pluginsElement.addContent(pluginElement);
        pluginElement.addContent(new Element("groupId", xmlns).setText("org.springframework.boot"));
        pluginElement.addContent(new Element("artifactId", xmlns).setText("spring-boot-maven-plugin"));

        // Generate string
        Format format = Format.getPrettyFormat();
        format.setEncoding("UTF-8");
        XMLOutputter xmlOutputter = new XMLOutputter(format);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        try {
            xmlOutputter.output(document, byteArrayOutputStream);
        } catch (IOException e) {
            LogUtil.warn(e.getMessage());
        }
        return byteArrayOutputStream.toString();
    }

    public String stringifyService(Table table) {
        JavaTemplate javaTemplate = new JavaTemplate(templatePath + "service/Service.java");
        javaTemplate.preprocessForSpringBootProject(project, table);

        if (!export.getBooleanProperty(SpringBootBackendProperty.PAGE_HELPER_ENABLE)) {
            javaTemplate.removeImport("com.github.pagehelper");
            for (MethodDeclaration method : javaTemplate.getCompilationUnit().getInterfaceByName(
                    table.getCapitalizedCamelName() + "Service").get().getMethods()) {
                if (method.getNameAsString().equals("retrieveList") && !method.getParameters().isEmpty()) {
                    method.remove();
                    break;
                }
            }
        }

        return javaTemplate.toString();
    }

    public String stringifyServiceImpl(Table table) {
        JavaTemplate javaTemplate = new JavaTemplate(templatePath + "service/impl/ServiceImpl.java");
        javaTemplate.preprocessForSpringBootProject(project, table);

        if (export.getBooleanProperty(SpringBootBackendProperty.PAGE_HELPER_ENABLE)) {

        } else {
            javaTemplate.removeImport("com.github.pagehelper");
            try {
                List<MethodDeclaration> methods =
                        javaTemplate.getCompilationUnit().getClassByName(table.getCapitalizedCamelName() + "ServiceImpl").get().getMethods();
                for (MethodDeclaration method : methods) {
                    if (method.getNameAsString().equals("retrieveList") && !method.getParameters().isEmpty()) {
                        method.remove();
                    }
                }
            } catch (Exception e) {
                LogUtil.error(-1, e.getMessage() + "\nClass Name: " + table.getCapitalizedCamelName() + "ServiceImpl");
            }

        }

        return javaTemplate.toString();
    }

    public String stringifyUtil(String utilName) {
        JavaTemplate javaTemplate = new JavaTemplate(templatePath + "util/" + utilName + ".java");
        javaTemplate.preprocessForSpringBootProject(project, null);
        return javaTemplate.toString();
    }

    public String stringifyVO(String VOName) {
        JavaTemplate javaTemplate = new JavaTemplate(templatePath + "VO/" + VOName + ".java");
        javaTemplate.preprocessForSpringBootProject(project, null);
        return javaTemplate.toString();
    }
}
