package com.mekcone.excrud.codegen.controller.generator;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.mekcone.excrud.codegen.constant.ModuleType;
import com.mekcone.excrud.codegen.constant.ModuleExtensionType;
import com.mekcone.excrud.codegen.controller.extmgr.springboot.LombokExtensionManager;
import com.mekcone.excrud.codegen.controller.extmgr.springboot.Swagger2ExtensionManager;
import com.mekcone.excrud.codegen.controller.parser.PropertiesParser;
import com.mekcone.excrud.codegen.controller.parser.template.impl.JavaTemplate;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Column;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Database;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Table;
import com.mekcone.excrud.codegen.model.module.impl.springboot.SpringBootModule;
import com.mekcone.excrud.codegen.model.module.impl.springboot.component.ProjectObjectModel;
import com.mekcone.excrud.codegen.model.module.impl.springboot.component.SpringBootComponent;
import com.mekcone.excrud.codegen.model.module.impl.springboot.component.SpringBootDataClass;
import com.mekcone.excrud.codegen.model.module.impl.springboot.component.SpringBootExtension;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SpringBootGenerator extends BaseGenerator {
    @Getter
    private SpringBootModule springBootModule;

    public PropertiesParser applicationPropertiesParser = new PropertiesParser();

    public SpringBootGenerator(Project project) {
        initialize(project, ModuleType.SPRING_BOOT);
        springBootModule = project.getModuleSet().getSpringBootModule();
        springBootModule.setGroupId(project.getGroupId());
        springBootModule.setArtifactId(project.getArtifactId());
    }

    public void generate() {
        copyInitialTemplates();

        for (Table table : project.getModuleSet().getRelationalDatabaseModule().getDatabases().get(0).getTables()) {
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
        PropertiesParser applicationPropertiesParser = springBootModule.getApplicationPropertiesParser();

        int serverPort = springBootModule.getProperties().getServerPort();
        if (serverPort > -1 && serverPort < 65536) {
            applicationPropertiesParser.add("server.port", Integer.toString(serverPort));
        }
        applicationPropertiesParser.addSeparator();


        Database defaultDatabase = project.getModuleSet().getRelationalDatabaseModule().getDatabases().get(0);
        String dataSourceUrl = "jdbc:" + defaultDatabase.getType() + "://" + defaultDatabase.getHost() + "/" + defaultDatabase.getName();
        if (defaultDatabase.getTimezone() != null) {
            dataSourceUrl += "?serverTimezone=" + defaultDatabase.getTimezone();
        }
        applicationPropertiesParser.add("spring.datasource.url", dataSourceUrl);
        applicationPropertiesParser.add("spring.datasource.username", defaultDatabase.getUsername());
        applicationPropertiesParser.add("spring.datasource.password", defaultDatabase.getPassword());
        applicationPropertiesParser.addSeparator();

        // Remove disabled extensions
        for (SpringBootExtension springBootExtension : springBootModule.getExtensions()) {
            if (!springBootExtension.isUse()) {
                removeExtension(springBootExtension.getId());
            }
        }

        log.info("Enabled extensions: {}", getExtensions().toString());

        // Run extension manager
        springBootModule.setProjectObjectModel(new ProjectObjectModel(project));

        for (String extensionName : getExtensions()) {
            switch (extensionName) {
                case ModuleExtensionType.LOMBOK:
                    new LombokExtensionManager(springBootModule);
                    break;
                case ModuleExtensionType.SWAGGER2:
                    new Swagger2ExtensionManager(this, project);
                    break;
                default:
                    ;
            }
        }

        // Write to files
        write();

        log.info("Generate {} completed", ModuleType.SPRING_BOOT);
    }

    @Override
    public void write() {
        // Application entry
        JavaTemplate javaTemplate = new JavaTemplate(templatePath + "Application.java");
        if (javaTemplate != null) {
            javaTemplate.preprocessForSpringBootProject(project, null);
        } else {
            log.info("Application.java not found");
        }
        javaTemplate.insert("ArtifactId", StrUtil.capitalize(project.getArtifactId()));
        addOutputFile(getPath("srcPath") + StrUtil.upperCamelCase(project.getArtifactId()) + "Application.java", javaTemplate.toString());

        // Application properties
        addOutputFile(getPath("resourcesPath") + "application.properties", springBootModule.getApplicationPropertiesParser().generate());

        // Controllers
        for (SpringBootComponent controllerComponent: springBootModule.getControllers()) {
            addOutputFile(getPath("controllerPath") + controllerComponent.getName() + ".java", controllerComponent.toString());
        }

        // Entities
        for (SpringBootDataClass entityBean: springBootModule.getEntities()) {
            addOutputFile(getPath("entityPath") + entityBean.getName() + ".java", entityBean.toString());
        }

        // Mappers
        for (SpringBootComponent mybatisMapperComponent: springBootModule.getMybatisMappers()) {
            addOutputFile(getPath("mapperPath") + mybatisMapperComponent.getName() + ".java", mybatisMapperComponent.toString());
        }

        // POM
        addOutputFile("pom.xml", springBootModule.getProjectObjectModel().toString());

        // Services
        for (SpringBootComponent serviceComponent: springBootModule.getServices()) {
            addOutputFile(getPath("servicePath") + serviceComponent.getName() + ".java", serviceComponent.toString());
        }

        // ServiceImpls
        for (SpringBootComponent serviceImplComponent: springBootModule.getServiceImpls()) {
            addOutputFile(getPath("serviceImplPath") + serviceImplComponent.getName() + ".java", serviceImplComponent.toString());
        }

        super.write();
    }

    public void createControllerComponent(Table table) {
        String controllerTemplatePath = templatePath + "controller/Controller.java";
        SpringBootComponent controllerComponent = new SpringBootComponent(controllerTemplatePath, project, table);
        springBootModule.addController(controllerComponent);
    }

    public void createEntityBean(Table table) {
        SpringBootDataClass entityBean = new SpringBootDataClass(project, table);
        springBootModule.addEntity(entityBean);
    }

    public void createMybatisMapperComponent(Table table) {
        if (!table.hasPrimaryKey()) {
            log.warn("Mapper interface cannot be generated from a table without a primary key");
            return;
        }

        String mybatisMapperTemplatePath = templatePath + "mapper/Mapper.java";
        SpringBootComponent mybatisMapperComponent = new SpringBootComponent(mybatisMapperTemplatePath, project, table);
        springBootModule.addMybatisMapper(mybatisMapperComponent);

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
        springBootModule.addService(serviceComponent);
    }

    public void createServiceImplComponent(Table table) {
        String serviceImplTemplatePath = templatePath + "service/impl/ServiceImpl.java";
        SpringBootComponent serviceImplComponent = new SpringBootComponent(serviceImplTemplatePath, project, table);
        springBootModule.addServiceImpl(serviceImplComponent);
    }
}
