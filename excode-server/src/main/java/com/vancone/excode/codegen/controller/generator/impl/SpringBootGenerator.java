package com.vancone.excode.codegen.controller.generator.impl;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vancone.excode.codegen.model.database.Column;
import com.vancone.excode.codegen.model.database.Database;
import com.vancone.excode.codegen.model.database.Table;
import com.vancone.excode.codegen.model.module.Module;
import com.vancone.excode.codegen.model.project.Project;
import com.vancone.excode.constant.ModuleConstant;
import com.vancone.excode.codegen.controller.generator.Generator;
import com.vancone.excode.codegen.controller.extmgr.datasource.SqlExtensionManager;
import com.vancone.excode.codegen.controller.extmgr.springboot.CrossOriginExtensionManager;
import com.vancone.excode.codegen.controller.extmgr.springboot.LombokExtensionManager;
import com.vancone.excode.codegen.controller.extmgr.springboot.MekConeCloudExtensionManager;
import com.vancone.excode.codegen.controller.extmgr.springboot.Swagger2ExtensionManager;
import com.vancone.excode.codegen.controller.parser.PropertiesParser;
import com.vancone.excode.codegen.controller.parser.template.impl.JavaTemplate;
import com.vancone.excode.codegen.model.module.impl.DatasourceModule;
import com.vancone.excode.codegen.model.module.impl.SpringBootModule;
import com.vancone.excode.codegen.model.file.springboot.MavenProjectObjectModel;
import com.vancone.excode.codegen.model.file.springboot.SpringBootComponent;
import com.vancone.excode.codegen.model.file.springboot.SpringBootDataClass;
import com.vancone.excode.codegen.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Tenton Lien
 */

@Slf4j
public class SpringBootGenerator extends Generator {

    public SpringBootGenerator(Project project) {
        super(project);
        module.asSpringBootModule().setGroupId(project.getGroupId());
        module.asSpringBootModule().setArtifactId(project.getArtifactId());
        module.asSpringBootModule().setMavenProjectObjectModel(new MavenProjectObjectModel(project));
    }

    public void generate() {
        SpringBootModule springBootModule = module.asSpringBootModule();
        for (Table table : project.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases().get(0).getTables()) {
            if (table.getCatalogueOf() != null && !table.getCatalogueOf().isEmpty()) {
                continue;
            }
            createControllerComponent(table);
            createEntityBean(table);
            createMybatisMapperComponent(table);
            createServiceComponent(table);
            createServiceImplComponent(table);
        }

        // Application entry class
        createApplicationEntry();

        // Application properties
        springBootModule.getMavenProjectObjectModel().addDependencies("mybatis");
        PropertiesParser applicationPropertiesParser = springBootModule.getApplicationPropertiesParser();

        int serverPort = Integer.parseInt(((SpringBootModule)module).getProperties().get(ModuleConstant.SPRING_BOOT_PROPERTY_SERVER_PORT));
        if (serverPort > -1 && serverPort < 65536) {
            applicationPropertiesParser.add("server.port", Integer.toString(serverPort));
        }
        applicationPropertiesParser.addSeparator();

        // Add relational database source
        Database defaultDatabase = project.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases().get(0);
        String dataSourceUrl = "jdbc:" + defaultDatabase.getType() + "://" + defaultDatabase.getHost() + "/" + defaultDatabase.getName();
        dataSourceUrl += defaultDatabase.getTimezone() != null ? "?serverTimezone=" + defaultDatabase.getTimezone() : "";
        applicationPropertiesParser.add("spring.datasource.url", dataSourceUrl);
        applicationPropertiesParser.add("spring.datasource.username", defaultDatabase.getUsername());
        applicationPropertiesParser.add("spring.datasource.password", defaultDatabase.getPassword());
        applicationPropertiesParser.addSeparator();

        // Add Redis source
        DatasourceModule.Redis redis = project.getModuleSet().getDatasourceModule().getRedis();
        if (!redis.getNodes().isEmpty()) {
            springBootModule.getMavenProjectObjectModel().addDependencies("redis");
            applicationPropertiesParser.add("spring.redis.database", redis.getNodes().get(0).getDatabase());
            applicationPropertiesParser.add("spring.redis.host", redis.getNodes().get(0).getHost());
            applicationPropertiesParser.add("spring.redis.port", String.valueOf(redis.getNodes().get(0).getPort()));
            if (!redis.getNodes().get(0).getPassword().isEmpty()) {
                applicationPropertiesParser.add("spring.redis.password", redis.getNodes().get(0).getPassword());
            }
            applicationPropertiesParser.addSeparator();

        } else {
            log.warn("No available Redis instance");
        }

        // Remove disabled extensions
        springBootModule.getExtensions().asList().removeIf(extension -> extension == null || !extension.isUse());

        List<String> enableExtensionNames = new ArrayList<>();
        for (Module.Extension extension: springBootModule.getExtensions().asList()) {
            if (extension != null) {
                enableExtensionNames.add(extension.getId());
            }
        }

        log.info("Enable extensions: {}", StringUtils.join(enableExtensionNames));

        // Run extension manager
        springBootModule.getExtensions().asList().forEach(extension -> {
            if (extension == null) return;
            log.info(StringUtils.center("ext::" + extension.getId(), 100, "-"));

            switch (extension.getId()) {
                case ModuleConstant.SPRING_BOOT_EXTENSION_CROSS_ORIGIN:
                    new CrossOriginExtensionManager(this, project);
                    break;
                case ModuleConstant.SPRING_BOOT_EXTENSION_LOMBOK:
                    new LombokExtensionManager(springBootModule);
                    break;
                case ModuleConstant.SPRING_BOOT_EXTENSION_MEKCONE_CLOUD:
                    new MekConeCloudExtensionManager(project);
                    break;
                case ModuleConstant.SPRING_BOOT_EXTENSION_SWAGGER2:
                    new Swagger2ExtensionManager(this, project);
                    break;
                default:
                    log.warn("Unknown extension: {}", extension.getId());
            }

            log.info("Execute extension {} complete", extension.getId());
        });

        write();
    }

    @Override
    public void write() {
        SpringBootModule springBootModule = module.asSpringBootModule();

        // Application entry
        addOutputFile(getPath("srcPath") + StrUtil.upperCamelCase(project.getArtifactId()) + "Application.java", springBootModule.getApplicationEntry().toString());

        // Application properties
        addOutputFile(getPath("resourcesPath") + "application.properties", springBootModule.getApplicationPropertiesParser().generate());

        // Controllers
        springBootModule.getControllers().forEach(controllerComponent ->
            addOutputFile(getPath("controllerPath") + controllerComponent.getName() + ".java", controllerComponent.toString()));

        // Entities
        springBootModule.getEntities().forEach(entityBean ->
            addOutputFile(getPath("entityPath") + entityBean.getName() + ".java", entityBean.toString()));

        // Mappers
        springBootModule.getMybatisMappers().forEach(mybatisMapperComponent ->
            addOutputFile(getPath("mapperPath") + mybatisMapperComponent.getName() + ".java", mybatisMapperComponent.toString()));

        // POM
        addOutputFile("pom.xml", springBootModule.getMavenProjectObjectModel().toString());

        // Services
        springBootModule.getServices().forEach(serviceComponent ->
            addOutputFile(getPath("servicePath") + serviceComponent.getName() + ".java", serviceComponent.toString()));

        // ServiceImpls
        springBootModule.getServiceImpls().forEach(serviceImplComponent ->
            addOutputFile(getPath("serviceImplPath") + serviceImplComponent.getName() + ".java", serviceImplComponent.toString()));

        // Write files
        super.write();
    }

    public void createApplicationEntry() {
        JavaTemplate javaTemplate = new JavaTemplate(getTemplatePath() + "Application.java");
        javaTemplate.preprocessForSpringBootProject(project, null);
        javaTemplate.insert("ArtifactId", StrUtil.capitalize(project.getArtifactId()));
        module.asSpringBootModule().setApplicationEntry(javaTemplate);
    }

    public void createControllerComponent(Table table) {
        String controllerTemplatePath = getTemplatePath() + "controller/Controller.java";
        SpringBootComponent controllerComponent = new SpringBootComponent(controllerTemplatePath, project, table);
        module.asSpringBootModule().addController(controllerComponent);
    }

    public void createEntityBean(Table table) {
        SpringBootDataClass entityBean = new SpringBootDataClass(project, table);
        module.asSpringBootModule().addEntity(entityBean);
    }

    public void createMybatisMapperComponent(Table table) {
        if (!table.hasPrimaryKey()) {
            log.warn("Mapper interface cannot be generated from a table without a primary key");
            return;
        }

        String mybatisMapperTemplatePath = getTemplatePath() + "mapper/Mapper.java";
        SpringBootComponent mybatisMapperComponent = new SpringBootComponent(mybatisMapperTemplatePath, project, table);
        module.asSpringBootModule().addMybatisMapper(mybatisMapperComponent);

        JavaTemplate javaTemplate = mybatisMapperComponent.getJavaTemplate();
        javaTemplate.preprocessForSpringBootProject(project, table);

        for (MethodDeclaration methodDeclaration : javaTemplate.getCompilationUnit().getInterfaceByName(table.getUpperCamelCaseName() + "Mapper").get().getMethods()) {
            NodeList<AnnotationExpr> annotations = methodDeclaration.getAnnotations();
            for (AnnotationExpr annotationExpr : annotations) {
                if (annotationExpr.getNameAsString().equals("Insert")) {
                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
                            SqlExtensionManager.insertQuery(table, true)));
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
                } else if (annotationExpr.getNameAsString().equals("Update")) {
                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
                            SqlExtensionManager.updateQuery(table)));
                }
            }
        }
    }

    public void createServiceComponent(Table table) {
        String serviceTemplatePath = getTemplatePath() + "service/Service.java";
        SpringBootComponent serviceComponent = new SpringBootComponent(serviceTemplatePath, project, table);
        module.asSpringBootModule().addService(serviceComponent);
    }

    public void createServiceImplComponent(Table table) {
        String serviceImplTemplatePath = getTemplatePath() + "service/impl/ServiceImpl.java";
        SpringBootComponent serviceImplComponent = new SpringBootComponent(serviceImplTemplatePath, project, table);
        module.asSpringBootModule().addServiceImpl(serviceImplComponent);
    }
}
