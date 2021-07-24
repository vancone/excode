package com.vancone.excode.core.old.controller.generator.impl;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vancone.excode.core.old.controller.extmgr.springboot.CrossOriginExtensionManager;
import com.vancone.excode.core.old.controller.extmgr.springboot.VanConeCloudExtensionManager;
import com.vancone.excode.core.old.controller.extmgr.springboot.Swagger2ExtensionManager;
import com.vancone.excode.core.old.model.database.Column;
import com.vancone.excode.core.old.model.database.Table;
import com.vancone.excode.core.model.MavenPom;
import com.vancone.excode.core.old.model.module.ModuleOld;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.old.constant.ModuleConstant;
import com.vancone.excode.core.old.controller.extmgr.datasource.SqlExtensionManager;
import com.vancone.excode.core.old.controller.extmgr.springboot.LombokExtensionManager;
import com.vancone.excode.core.old.controller.generator.Generator;
import com.vancone.excode.core.PropertiesParser;
import com.vancone.excode.core.old.controller.parser.template.impl.JavaTemplate;
import com.vancone.excode.core.old.model.database.Database;
import com.vancone.excode.core.old.model.module.impl.DatasourceModule;
import com.vancone.excode.core.old.model.module.impl.SpringBootModule;
import com.vancone.excode.core.old.model.file.springboot.SpringBootComponent;
import com.vancone.excode.core.model.SpringBootDataClass;
import com.vancone.excode.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Slf4j
public class SpringBootGenerator extends Generator {

    public SpringBootGenerator(ProjectOld projectOld) {
        super(projectOld);
        module.asSpringBootModule().setGroupId(projectOld.getGroupId());
        module.asSpringBootModule().setArtifactId(projectOld.getArtifactId());
        module.asSpringBootModule().setMavenPom(new MavenPom(projectOld));
    }

    public void generate() {
        SpringBootModule springBootModule = module.asSpringBootModule();
        for (Table table : projectOld.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases().get(0).getTables()) {
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
        springBootModule.getMavenPom().addDependenciesOld("mybatis");
        PropertiesParser applicationPropertiesParser = springBootModule.getApplicationPropertiesParser();

        int serverPort = ((SpringBootModule)module).getProperties().getServerPort();
        if (serverPort > -1 && serverPort < 65536) {
            applicationPropertiesParser.add("server.port", Integer.toString(serverPort));
        }
        applicationPropertiesParser.addSeparator();

        // Add relational database source
        Database defaultDatabase = projectOld.getModuleSet().getDatasourceModule().getRelationalDatabase().getDatabases().get(0);
        String dataSourceUrl = "jdbc:" + defaultDatabase.getType() + "://" + defaultDatabase.getHost() + "/" + defaultDatabase.getName();
        dataSourceUrl += defaultDatabase.getTimezone() != null ? "?serverTimezone=" + defaultDatabase.getTimezone() : "";
        applicationPropertiesParser.add("spring.datasource.url", dataSourceUrl);
        applicationPropertiesParser.add("spring.datasource.username", defaultDatabase.getUsername());
        applicationPropertiesParser.add("spring.datasource.password", defaultDatabase.getPassword());
        applicationPropertiesParser.addSeparator();

        // Add Redis source
        DatasourceModule.Redis redis = projectOld.getModuleSet().getDatasourceModule().getRedis();
        if (!redis.getNodes().isEmpty()) {
            springBootModule.getMavenPom().addDependenciesOld("redis");
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
        springBootModule.getExtensions().removeIf(extension -> !extension.isUse());

        List<String> enableExtensionNames = new ArrayList<>();
        for (ModuleOld.Extension extension: springBootModule.getExtensions()) {
            enableExtensionNames.add(extension.getId());
        }

        log.info("Enable extensions: {}", StringUtils.join(enableExtensionNames));

        // Run extension manager
        springBootModule.getExtensions().forEach(extension -> {
            if (extension.getId() == null) {
                log.error("extension id is null: {}", extension.toString());
            }
            log.info(StringUtils.center("ext::" + extension.getId(), 100, "-"));

            switch (extension.getId()) {
                case ModuleConstant.SPRING_BOOT_EXTENSION_CROSS_ORIGIN:
                    new CrossOriginExtensionManager(this, projectOld);
                    break;
                case ModuleConstant.SPRING_BOOT_EXTENSION_LOMBOK:
                    new LombokExtensionManager(springBootModule);
                    break;
                case ModuleConstant.SPRING_BOOT_EXTENSION_MEKCONE_CLOUD:
                    new VanConeCloudExtensionManager(projectOld);
                    break;
                case ModuleConstant.SPRING_BOOT_EXTENSION_SWAGGER2:
                    new Swagger2ExtensionManager(this, projectOld);
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
        addOutputFile(getPath("srcPath") + StrUtil.upperCamelCase(projectOld.getArtifactId()) + "Application.java", springBootModule.getApplicationEntry().toString());

        // Application properties
        addOutputFile(getPath("resourcesPath") + "application.properties", springBootModule.getApplicationPropertiesParser().generate());

        // Controllers
        springBootModule.getControllers().forEach(controllerComponent ->
            addOutputFile(getPath("controllerPath") + controllerComponent.getName() + ".java", controllerComponent.toString()));

        // Entities
        springBootModule.getEntities().forEach(entityBean ->
            addOutputFile(getPath("entityPath") + entityBean.getName() + ".java", entityBean.toStringOld()));

        // Mappers
        springBootModule.getMybatisMappers().forEach(mybatisMapperComponent ->
            addOutputFile(getPath("mapperPath") + mybatisMapperComponent.getName() + ".java", mybatisMapperComponent.toString()));

        // POM
        addOutputFile("pom.xml", springBootModule.getMavenPom().toString());

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
        javaTemplate.preprocessForSpringBootProject(projectOld, null);
        javaTemplate.insert("ArtifactId", StrUtil.capitalize(projectOld.getArtifactId()));
        module.asSpringBootModule().setApplicationEntry(javaTemplate);
    }

    public void createControllerComponent(Table table) {
        String controllerTemplatePath = getTemplatePath() + "controller/Controller.java";
        SpringBootComponent controllerComponent = new SpringBootComponent(controllerTemplatePath, projectOld, table);
        module.asSpringBootModule().addController(controllerComponent);
    }

    public void createEntityBean(Table table) {
        SpringBootDataClass entityBean = new SpringBootDataClass(projectOld, table);
        module.asSpringBootModule().addEntity(entityBean);
    }

    public void createMybatisMapperComponent(Table table) {
        if (!table.hasPrimaryKey()) {
            log.warn("Mapper interface cannot be generated from a table without a primary key");
            return;
        }

        String mybatisMapperTemplatePath = getTemplatePath() + "mapper/Mapper.java";
        SpringBootComponent mybatisMapperComponent = new SpringBootComponent(mybatisMapperTemplatePath, projectOld, table);
        module.asSpringBootModule().addMybatisMapper(mybatisMapperComponent);

        JavaTemplate javaTemplate = mybatisMapperComponent.getJavaTemplate();
        javaTemplate.preprocessForSpringBootProject(projectOld, table);

        for (MethodDeclaration methodDeclaration : javaTemplate.getCompilationUnit().getInterfaceByName(table.getUpperCamelCaseName() + "Mapper").get().getMethods()) {
            NodeList<AnnotationExpr> annotations = methodDeclaration.getAnnotations();
            for (AnnotationExpr annotationExpr : annotations) {
                if (annotationExpr.getNameAsString().equals("Insert")) {
                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
                            SqlExtensionManager.insertQueryOld(table, true)));
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
                            SqlExtensionManager.updateQueryOld(table)));
                }
            }
        }
    }

    public void createServiceComponent(Table table) {
        String serviceTemplatePath = getTemplatePath() + "service/Service.java";
        SpringBootComponent serviceComponent = new SpringBootComponent(serviceTemplatePath, projectOld, table);
        module.asSpringBootModule().addService(serviceComponent);
    }

    public void createServiceImplComponent(Table table) {
        String serviceImplTemplatePath = getTemplatePath() + "service/impl/ServiceImpl.java";
        SpringBootComponent serviceImplComponent = new SpringBootComponent(serviceImplTemplatePath, projectOld, table);
        module.asSpringBootModule().addServiceImpl(serviceImplComponent);
    }
}
