package com.mekcone.excrud.generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.mekcone.excrud.constant.ExportTypes;
import com.mekcone.excrud.constant.JavaWords;
import com.mekcone.excrud.constant.SpringBootProject;
import com.mekcone.excrud.enums.ErrorEnums;
import com.mekcone.excrud.loader.model.Project;
import com.mekcone.excrud.loader.model.components.*;
import com.mekcone.excrud.loader.model.data.Column;
import com.mekcone.excrud.loader.model.data.Database;
import com.mekcone.excrud.loader.model.data.Table;
import com.mekcone.excrud.model.springboot.PluginInfo;
import com.mekcone.excrud.model.template.JavaTemplate;
import com.mekcone.excrud.model.template.PlainTemplate;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import com.mekcone.excrud.util.SqlUtil;
import com.mekcone.excrud.util.StringUtil;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SpringBootBackendGenerator {

    private final String EXCRUD_HOME = Application.getHomeDirectory();
    private final String templatePath = EXCRUD_HOME + "/exports/" + ExportTypes.SPRING_BOOT_BACKEND + "/templates/";

    private String springBootBackendPath;

    private Project project;

    private Export export;

    public SpringBootBackendGenerator(Project project, Export export) {
        this.project = project;
        this.export = export;
    }

    public boolean build() {
        return false;
    }

    private void createDirectories() {
        if (EXCRUD_HOME == null) {
            LogUtil.error(ErrorEnums.EXCRUD_HOME_ENV_VARIABLE_NOT_SET);
        }

        List<String> paths = FileUtil.readLine(EXCRUD_HOME + "/exports/" + ExportTypes.SPRING_BOOT_BACKEND + "/directories.txt");
        if (paths == null) {
            LogUtil.error(ErrorEnums.SBP_DIRECTORIES_TXT_NOT_FOUND);
        }
        String sourcePath = ExportTypes.SPRING_BOOT_BACKEND + "/src/main/java/" + (project.getGroupId() + "." +
                project.getArtifactId()).replace('.', '/');
        for (String path : paths) {
            path = path.replace("SRC_PATH", sourcePath);
            FileUtil.checkDirectory(path);
        }
    }

    private void preprocessTemplate(PlainTemplate plainTemplate, Table table) {
        String templateText = plainTemplate.toString();
        if (templateText.indexOf("## " + SpringBootProject.GROUP_ID + " ##") > -1) {
            plainTemplate.insert(SpringBootProject.GROUP_ID, project.getGroupId());
        }
        if (templateText.indexOf("## " + SpringBootProject.ARTIFACT_ID + " ##") > -1) {
            plainTemplate.insert(SpringBootProject.ARTIFACT_ID, project.getArtifactId());
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
        if (templateText.indexOf("__" + SpringBootProject.GROUP_ID + "__") > -1) {
            javaTemplate.insert(SpringBootProject.GROUP_ID, project.getGroupId());
        }
        if (templateText.indexOf("__" + SpringBootProject.ARTIFACT_ID + "__") > -1) {
            javaTemplate.insert(SpringBootProject.ARTIFACT_ID, project.getArtifactId());
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
        springBootBackendPath = ExportTypes.SPRING_BOOT_BACKEND + "/";
        String resourcesPath = ExportTypes.SPRING_BOOT_BACKEND + "/src/main/resources/";
        String sourcePath = springBootBackendPath + "src/main/java/" + (project.getGroupId() + "." + project.getArtifactId()).replace('.', '/') + "/";

        FileUtil.checkDirectory(resourcesPath);

        FileUtil.write(springBootBackendPath + "pom.xml", stringifyPomXml());
        FileUtil.write(resourcesPath + "application.properties", stringifyApplicationProperties());
        FileUtil.write(sourcePath + StringUtil.capitalizedCamel(project.getArtifactId()) + "Application.java", stringifyApplicationEntry());
        FileUtil.write(springBootBackendPath + project.getArtifactId() + ".sql", stringifySql());

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

        /*if (export.getProperty("cross_origin_enable").equals("true")) {
            LogUtil.error(-1, "exist cors");
        } else {
            LogUtil.error(-1, "not exist cors");
        }*/
        /*if (export.getPlugins() != null) {
            for (Plugin plugin : export.getPlugins()) {
                String configText = stringifyConfig(plugin);
                if (configText != null) {
                    String filePath = configPath + StringUtil.capitalizedCamel(plugin.getName()) + "Config.java";
                    if (plugin.getName().equals("spring-boot")) {
                        filePath = filePath = configPath + "CrossOriginConfig.java";
                    }
                    FileUtil.write(filePath, configText);
                }
            }
        }*/

        // Utils
        String utilPath = sourcePath + "util/";
        FileUtil.write(utilPath + "ResultVOUtil.java", stringifyUtil("ResultVOUtil"));

        // VOs
        String VOPath = sourcePath + "VO/";
        FileUtil.write(VOPath + "ResultVO.java", stringifyVO("ResultVO"));

        LogUtil.info("Generate " + ExportTypes.SPRING_BOOT_BACKEND + " completed");

        // throw new ExportException(ExportExceptionEnums.GENERATE_PROJECT_FAILED);
    }


    public boolean run() {
        return false;
    }


    public String stringifyApplicationEntry() {
        PlainTemplate plainTemplate = new PlainTemplate(ExportTypes.SPRING_BOOT_BACKEND, "Application.java");
        preprocessTemplate(plainTemplate);
        plainTemplate.insert("ArtifactId", StringUtil.capitalize(project.getArtifactId()));
        return plainTemplate.toString();
    }


    public String stringifyApplicationProperties() {
        String applicationPropertiesText = "";
        List<String> pluginNames = new ArrayList<>();
        if (export.getProperty("mybatis_enable").equals("true")) {
            pluginNames.add("mybatis");
        }

        if (export.getProperty("page_helper_enable").equals("true")) {
            pluginNames.add("page-helper");
        }


        for (String pluginName : pluginNames) {
            String pluginText = FileUtil.read(EXCRUD_HOME + "/exports/" + ExportTypes.SPRING_BOOT_BACKEND + "/plugins/" + pluginName + ".json");
            if (pluginText == null) {
                continue;
            }
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                PluginInfo pluginInfo = objectMapper.readValue(pluginText, PluginInfo.class);
                List<Property> properties = pluginInfo.getProperties();
                if (properties != null) {
                    applicationPropertiesText += "# " + pluginName + "\n";
                    for (Property property : properties) {
                        applicationPropertiesText += property.getKey() + " = ";
                        if (property.getDefaultValue() != null) {
                            applicationPropertiesText += property.getDefaultValue();
                        }
                        applicationPropertiesText += "\n";
                    }
                    applicationPropertiesText += "\n";
                }
            } catch (JsonProcessingException e) {
                LogUtil.warn(e.getMessage());
            }
        }


        // Database properties
        applicationPropertiesText += "# database\n";
        if (export.getProperty("mybatis_enable").equals("true")) {
            Database database = project.getDatabases().get(0);
            applicationPropertiesText += "spring.datasource.url = jdbc:" + database.getType() + "://" + database.getHost() + "\n";
            applicationPropertiesText += "spring.datasource.username = " + database.getUsername() + "\n";
            applicationPropertiesText += "spring.datasource.password = " + database.getPassword() + "\n\n";
        } else if (export.getProperty("hibernate").equals("true")) {
            LogUtil.warn("Hibernate have not be supported yet.");
        } else {
            LogUtil.warn("No Mybatis or Hibernate module found.");
        }

        return applicationPropertiesText;
    }

    public String stringifyConfig(Plugin plugin) {
        if (export.getProperty("cross_origin_enable").equals("true")) {
            /*PlainTemplate plainTemplate = new PlainTemplate(EXPORT_TYPE, "config/CrossOriginConfig.java");
            preprocessTemplate(plainTemplate);

            List<String> headers = export.getAllowedHeaders();
            if (headers != null) {
                String allowedHeadersText = "";
                for (int i = 0; i < headers.size(); i++) {
                    allowedHeadersText += "\"" + headers.get(i) + "\"";
                    if (i + 1 != headers.size()) {
                        allowedHeadersText += ",";
                    }
                }
                plainTemplate.insert("allowedHeaders", ".allowedHeaders(" + allowedHeadersText + ")");
            } else {
                plainTemplate.remove("allowedHeaders");
            }

            List<String> methods = export.getAllowedMethods();
            if (methods != null) {
                String allowedMethodsText = "";
                for (int i = 0; i < methods.size(); i++) {
                    allowedMethodsText += "\"" + methods.get(i) + "\"";
                    if (i + 1 != methods.size()) {
                        allowedMethodsText += ",";
                    }
                }
                plainTemplate.insert("allowedMethods", ".allowedMethods(" + allowedMethodsText + ")");
            } else {
                plainTemplate.remove("allowedMethods");
            }

            List<String> origins = export.getAllowedOrigins();
            if (origins != null) {
                String allowedOriginsText = "";
                for (int i = 0; i < origins.size(); i++) {
                    allowedOriginsText += "\"" + origins.get(i) + "\"";
                    if (i + 1 != origins.size()) {
                        allowedOriginsText += ",";
                    }
                }
                plainTemplate.insert("allowedOrigins", ".allowedOrigins(" + allowedOriginsText + ")");
            } else {
                plainTemplate.remove("allowedOrigins");
            }

            return plainTemplate.toString();*/
        } else if (plugin.getName().equals(SpringBootProject.PLUGIN_SWAGGER2) && plugin.isEnable()) {
            PlainTemplate plainTemplate = new PlainTemplate(ExportTypes.SPRING_BOOT_BACKEND, "config/Swagger2Config.java");
            preprocessTemplate(plainTemplate);

            String title = project.getApiDocument().getTitle();
            if (title != null) {
                plainTemplate.insert("title", title);
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
        }
        return null;
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

        if (export.getProperty("page_helper_enable").equals("true")) {
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

        boolean lombokEnabled = false;
        if (export.getProperty("lombok_enable").equals("true")) {
            lombokEnabled = true;
        }

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
            if (!type.equals(JavaWords.INT)) {
                type = JavaWords.STRING;
            }

            entityClassDeclaration.addField(type, column.getCamelName(table.getName()), Modifier.Keyword.PRIVATE);

            if (!lombokEnabled) {

                // Getter
                MethodDeclaration getterMethodDeclaration =
                        entityClassDeclaration.addMethod("get" + column.getCapitalizedCamelName(table.getName()), Modifier.Keyword.PUBLIC);
                getterMethodDeclaration.setType(JavaWords.STRING);
                BlockStmt getterMethodBody = new BlockStmt();
                getterMethodBody.addAndGetStatement(new ReturnStmt(column.getCamelName(table.getName())));
                getterMethodDeclaration.setBody(getterMethodBody);

                // Setter
                MethodDeclaration setterMethodDeclaration =
                        entityClassDeclaration.addMethod("set" + column.getCapitalizedCamelName(table.getName()), Modifier.Keyword.PUBLIC);
                setterMethodDeclaration.setType(JavaWords.VOID);
                setterMethodDeclaration.addParameter(JavaWords.STRING, column.getCamelName(table.getName()));
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
                            SqlUtil.insertQuery(table, true)));
                } else if (annotation.getNameAsString().equals("Results")) {
                    ArrayInitializerExpr array = new ArrayInitializerExpr();
                    for (Column column : table.getColumns()) {
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
                            SqlUtil.updateQuery(table)));
                }
            }
        }

        return javaTemplate.toString();
    }

    public String stringifyPomXml() {
        // Root
        Element rootElement = new Element("project");
        Document document = new Document(rootElement);
        rootElement.addContent(new Comment("NO_DESCRIPTION"));
        Namespace xmlns = Namespace.getNamespace("http://maven.apache.org/POM/4.0.0");
        rootElement.setNamespace(xmlns);
        Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.addNamespaceDeclaration(xsi);
        rootElement.setAttribute("schemaLocation", "http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd", xsi);
        rootElement.addContent(new Element("modelVersion", xmlns).setText("4.0.0"));

        // Parent
        Element parentElement = new Element("parent", xmlns);
        rootElement.addContent(parentElement);
        parentElement.addContent(new Element(SpringBootProject.GROUP_ID, xmlns).setText("org.springframework.boot"));
        parentElement.addContent(new Element(SpringBootProject.ARTIFACT_ID, xmlns).setText("spring-boot-starter-parent"));
        parentElement.addContent(new Element(SpringBootProject.VERSION, xmlns).setText("2.2.1.RELEASE"));
        parentElement.addContent(new Element("relativePath", xmlns));

        rootElement.addContent(new Element(SpringBootProject.GROUP_ID, xmlns).setText(project.getGroupId()));
        rootElement.addContent(new Element(SpringBootProject.ARTIFACT_ID, xmlns).setText(project.getArtifactId()));
        rootElement.addContent(new Element(SpringBootProject.VERSION, xmlns).setText(project.getVersion()));
        rootElement.addContent(new Element("name", xmlns).setText(project.getArtifactId()));
        rootElement.addContent(new Element("description", xmlns).setText(
                StringUtil.capitalize(project.getArtifactId()) +
                        " Project auto-generated by MekCone ExCRUD"
        ));

        // Properties
        Element propertiesElement = new Element("properties", xmlns);
        rootElement.addContent(propertiesElement);
        propertiesElement.addContent(new Element("java.version", xmlns).setText("1.8"));

        // Dependencies
        List<Dependency> dependencies = new ArrayList<>();
        List<String> pluginNames = new ArrayList<>();
        if (export.getProperty("lombok_enable").equals("true")) {
            pluginNames.add("lombok");
        }

        if (export.getProperty("page_helper_enable").equals("true")) {
            pluginNames.add("page-helper");
        }

        pluginNames.add("spring-boot");

        if (export.getProperty("swagger2_enable").equals("true")) {
            pluginNames.add("swagger2");
        }


        for (String pluginName : pluginNames) {
            String pluginText = FileUtil.read(EXCRUD_HOME + "/exports/" + ExportTypes.SPRING_BOOT_BACKEND + "/plugins/" + pluginName + ".json");
            if (pluginText == null) {
                continue;
            }
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                PluginInfo pluginInfo = objectMapper.readValue(pluginText, PluginInfo.class);
                if (pluginInfo.getDependencies() != null) {
                    dependencies.addAll(pluginInfo.getDependencies());
                }
            } catch (JsonProcessingException e) {
                LogUtil.warn(e.getMessage());
            }
        }

        Element dependenciesElement = new Element("dependencies", xmlns);
        rootElement.addContent(dependenciesElement);

        for (Dependency dependency : dependencies) {
            Element dependencyElement = new Element("dependency", xmlns);
            dependencyElement.addContent(new Element(SpringBootProject.GROUP_ID, xmlns).setText(dependency.getGroupId()));
            dependencyElement.addContent(new Element(SpringBootProject.ARTIFACT_ID, xmlns).setText(dependency.getArtifactId()));
            if (dependency.getVersion() != null) {
                dependencyElement.addContent(new Element(SpringBootProject.VERSION, xmlns).setText(dependency.getVersion()));
            }
            if (dependency.getScope() != null) {
                dependencyElement.addContent(new Element(SpringBootProject.SCOPE, xmlns).setText(dependency.getScope()));
            }
            if (dependency.getExclusions() != null) {
                Element exclusionsElement = new Element("exclusions", xmlns);
                dependencyElement.addContent(exclusionsElement);
                Element exclusionElement = new Element("exclusion", xmlns);
                exclusionsElement.addContent(exclusionElement);
                exclusionElement.addContent(new Element("groupId", xmlns).setText("org.junit.vintage"));
                exclusionElement.addContent(new Element("artifactId", xmlns).setText("junit-vintage-engine"));
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

        if (!export.getProperty("page_helper_enable").equals("true")) {
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

        if (export.getProperty("page_helper_enable").equals("true")) {

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

    public String stringifySql() {
        String code = "-- NO DESCRIPTIONS" + "\n\n";
        for (Table table : project.getDatabases().get(0).getTables()) {
            code += "CREATE TABLE " + table.getName() + "\n(\n";
            for (int i = 0; i < table.getColumns().size(); i++) {
                code += table.getColumns().get(i).getName() + " " + table.getColumns().get(i).getType();
                if (table.getColumns().get(i).isPrimaryKey()) {
                    code += " NOT NULL AUTO_INCREMENT";
                }
                code += ",\n";
                if (i + 1 == table.getColumns().size()) {
                    code += "PRIMARY KEY(" + table.getPrimaryKey() + ")\n";
                }
            }
            code += ");\n\n";
        }
        return code;
    }
}
