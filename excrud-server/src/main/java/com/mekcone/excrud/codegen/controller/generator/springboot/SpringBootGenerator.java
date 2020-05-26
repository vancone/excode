package com.mekcone.excrud.codegen.controller.generator.springboot;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.mekcone.excrud.codegen.constant.basic.ExportType;
import com.mekcone.excrud.codegen.constant.extensions.SpringBootExtensionType;
import com.mekcone.excrud.codegen.controller.generator.BaseGenerator;
import com.mekcone.excrud.codegen.controller.generator.SqlGenerator;
import com.mekcone.excrud.codegen.controller.generator.springboot.extension.impl.LombokExtensionManager;
import com.mekcone.excrud.codegen.controller.generator.springboot.extension.impl.Swagger2ExtensionManager;
import com.mekcone.excrud.codegen.controller.parser.PropertiesParser;
import com.mekcone.excrud.codegen.controller.parser.template.impl.JavaTemplate;
import com.mekcone.excrud.codegen.controller.parser.template.impl.UniversalTemplate;
import com.mekcone.excrud.codegen.model.export.impl.relationaldatabase.component.Database;
import com.mekcone.excrud.codegen.model.export.impl.springboot.component.SpringBootExtension;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.model.export.impl.relationaldatabase.component.Column;
import com.mekcone.excrud.codegen.model.export.impl.relationaldatabase.component.Table;
import com.mekcone.excrud.codegen.model.export.impl.springboot.SpringBootGenModel;
import com.mekcone.excrud.codegen.model.export.impl.springboot.component.SpringBootDataClass;
import com.mekcone.excrud.codegen.model.export.impl.springboot.component.SpringBootComponent;
import com.mekcone.excrud.codegen.model.export.impl.springboot.component.ProjectObjectModel;
import com.mekcone.excrud.codegen.util.LogUtil;
import com.mekcone.excrud.codegen.util.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
public class SpringBootGenerator extends BaseGenerator {
    @Getter
    private SpringBootGenModel springBootGenModel;

    public PropertiesParser applicationPropertiesParser = new PropertiesParser();

    public SpringBootGenerator(Project project) {
        initialize(project, ExportType.SPRING_BOOT);
        springBootGenModel = project.getExports().getSpringBootGenModel();
        springBootGenModel.setGroupId(project.getGroupId());
        springBootGenModel.setArtifactId(project.getArtifactId());
    }

    public boolean build() {
        File file = new File(generatedDataPath);

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

        for (Table table : project.getExports().getRelationalDatabaseExport().getDatabases().get(0).getTables()) {
            if (table.getCatalogueOf() != null && !table.getCatalogueOf().isEmpty()) {
                continue;
            }
            createControllerComponent(table);
            createEntityBean(table);
            createMybatisMapperComponent(table);
            createServiceComponent(table);
            createServiceImplComponent(table);
        }

        // Application properties
        PropertiesParser applicationPropertiesParser = springBootGenModel.getApplicationPropertiesParser();

        int serverPort = springBootGenModel.getProperties().getServerPort();
        if (serverPort > -1 && serverPort < 65536) {
            applicationPropertiesParser.add("server.port", Integer.toString(serverPort));
        }
        applicationPropertiesParser.addSeparator();


        Database defaultDatabase = project.getExports().getRelationalDatabaseExport().getDatabases().get(0);
        String dataSourceUrl = "jdbc:" + defaultDatabase.getType() + "://" + defaultDatabase.getHost() + "/" + defaultDatabase.getName();
        if (defaultDatabase.getTimezone() != null) {
            dataSourceUrl += "?serverTimezone=" + defaultDatabase.getTimezone();
        }
        applicationPropertiesParser.add("spring.datasource.url", dataSourceUrl);
        applicationPropertiesParser.add("spring.datasource.username", defaultDatabase.getUsername());
        applicationPropertiesParser.add("spring.datasource.password", defaultDatabase.getPassword());
        applicationPropertiesParser.addSeparator();

        // Config

        // Cross origin
        /*if (oldExport.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_HEADERS) ||
                oldExport.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_METHODS) ||
                oldExport.existProperty(SpringBootBackendProperty.CROSS_ORIGIN_ALLOWED_ORIGINS)) {
            FileUtil.write(getPath("configPath") + "CrossOriginConfig.java", stringifyConfig("cross_origin"));
        }

        // Swagger2
        if (oldExport.getBooleanProperty(SpringBootBackendProperty.SWAGGER2_ENABLE)) {
            FileUtil.write(getPath("configPath") + "Swagger2Config.java", stringifyConfig("swagger2"));
        }*/

//        addOutputFile(getPath("resourcesPath") + "application.properties", "properties", applicationPropertiesParser.generate());

        // Remove disabled extensions
        for (SpringBootExtension springBootExtension : springBootGenModel.getExtensions()) {
            if (!springBootExtension.isUse()) {
                removeExtension(springBootExtension.getId());
            }
        }

        LogUtil.info("Enabled extensions: " + getExtensions().toString());

        // Run extension manager
        springBootGenModel.setProjectObjectModel(new ProjectObjectModel(project));

        for (String extensionName : getExtensions()) {
            switch (extensionName) {
                case SpringBootExtensionType.LOMBOK:
                    new LombokExtensionManager(springBootGenModel);
                    break;
                case SpringBootExtensionType.SWAGGER2:
                    new Swagger2ExtensionManager(springBootGenModel);
                    break;
                default:
                    ;
            }
        }

        // Write to files
        write();

        LogUtil.info("Generate " + ExportType.SPRING_BOOT + " completed");
    }

    @Override
    public void write() {
        // Application entry
        UniversalTemplate universalTemplate = new UniversalTemplate(templatePath + "Application.java");
        if (universalTemplate != null) {
            preprocessTemplate(universalTemplate);
        } else {
            LogUtil.info("Application.java not found");
        }
        universalTemplate.insert("ArtifactId", StrUtil.capitalize(project.getArtifactId()));
        addOutputFile(exportType + "/" + getPath("srcPath") + StrUtil.upperCamelCase(project.getArtifactId()) + "Application.java", universalTemplate.toString());

        // Application properties
//        LogUtil.info("AP: " + springBootGenModel.getApplicationPropertiesParser());
        addOutputFile(exportType + "/" + getPath("resourcesPath") + "application.properties", springBootGenModel.getApplicationPropertiesParser().generate());

        // Controllers
        for (SpringBootComponent controllerComponent: springBootGenModel.getControllers()) {
            addOutputFile(exportType + "/" + getPath("controllerPath") + controllerComponent.getName() + ".java", controllerComponent.toString());
        }

        // Entities
        for (SpringBootDataClass entityBean: springBootGenModel.getEntities()) {
            addOutputFile(exportType + "/" + getPath("entityPath") + entityBean.getName() + ".java", entityBean.toString());
        }

        // Mappers
        for (SpringBootComponent mybatisMapperComponent: springBootGenModel.getMybatisMappers()) {
            addOutputFile(exportType + "/" + getPath("mapperPath") + mybatisMapperComponent.getName() + ".java", mybatisMapperComponent.toString());
        }

        // POM
        addOutputFile(generatedDataPath + "pom.xml", springBootGenModel.getProjectObjectModel().toString());

        // Services
        for (SpringBootComponent serviceComponent: springBootGenModel.getServices()) {
            addOutputFile(exportType + "/" + getPath("servicePath") + serviceComponent.getName() + ".java", serviceComponent.toString());
        }

        // ServiceImpls
        for (SpringBootComponent serviceImplComponent: springBootGenModel.getServiceImpls()) {
            addOutputFile(exportType + "/" + getPath("serviceImplPath") + serviceImplComponent.getName() + ".java", serviceImplComponent.toString());
        }

        super.write();
    }


    public boolean run() {
        return false;
    }


    /*public String stringifyApplicationProperties() {
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
                    Database defaultDatabase = project.getExports().getRelationalDatabaseExport().getDatabases().get(0);
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
    }*/

    /*public String stringifyConfig(String configName) {
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
                List<Table> tables = project.getExports().getRelationalDatabaseExport().getDatabases().get(0).getTables();
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
    }*/

    public void createControllerComponent(Table table) {
        String controllerTemplatePath = templatePath + "controller/Controller.java";
        SpringBootComponent controllerComponent = new SpringBootComponent(controllerTemplatePath, project, table);
        springBootGenModel.addController(controllerComponent);
    }

    public void createEntityBean(Table table) {
        SpringBootDataClass entityBean = new SpringBootDataClass(project, table);
        springBootGenModel.addEntity(entityBean);
    }

    public void createMybatisMapperComponent(Table table) {
        if (!table.hasPrimaryKey()) {
            log.warn("Mapper interface cannot be generated from a table without a primary key");
            return;
        }

        String mybatisMapperTemplatePath = templatePath + "mapper/Mapper.java";
        SpringBootComponent mybatisMapperComponent = new SpringBootComponent(mybatisMapperTemplatePath, project, table);
        springBootGenModel.addMybatisMapper(mybatisMapperComponent);

        JavaTemplate javaTemplate = mybatisMapperComponent.getJavaTemplate();
        javaTemplate.preprocessForSpringBootProject(project, table);

        for (MethodDeclaration methodDeclaration : javaTemplate.getCompilationUnit().getInterfaceByName(table.getUpperCamelCaseName() + "Mapper").get().getMethods()) {
            NodeList<AnnotationExpr> annotations = methodDeclaration.getAnnotations();
            for (AnnotationExpr annotationExpr : annotations) {
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
    }

    public void createServiceComponent(Table table) {
        String serviceTemplatePath = templatePath + "service/Service.java";
        SpringBootComponent serviceComponent = new SpringBootComponent(serviceTemplatePath, project, table);
        springBootGenModel.addService(serviceComponent);
    }

    public void createServiceImplComponent(Table table) {
        String serviceImplTemplatePath = templatePath + "service/impl/ServiceImpl.java";
        SpringBootComponent serviceImplComponent = new SpringBootComponent(serviceImplTemplatePath, project, table);
        springBootGenModel.addServiceImpl(serviceImplComponent);
    }
}
