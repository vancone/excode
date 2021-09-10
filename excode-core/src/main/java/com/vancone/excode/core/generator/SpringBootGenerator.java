package com.vancone.excode.core.generator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vancone.excode.core.ProjectWriter;
import com.vancone.excode.core.PropertiesParser;
import com.vancone.excode.core.TemplateFactory;
import com.vancone.excode.core.constant.ExtensionType;
import com.vancone.excode.core.enums.OrmType;
import com.vancone.excode.core.enums.TemplateType;
import com.vancone.excode.core.extension.SpringBootExtensionHandler;
import com.vancone.excode.core.model.*;
import com.vancone.excode.core.model.datasource.MysqlDataSource;
import com.vancone.excode.core.util.CompilationUnitUtil;
import com.vancone.excode.core.util.SqlUtil;
import com.vancone.excode.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.shared.invoker.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 7/24/2021
 */
@Slf4j
public class SpringBootGenerator {

    private String packagePath;
    private ProjectWriter writer;
    private Project project;
    private Module module;

    private SpringBootGenerator(Module module, ProjectWriter writer) {
        this.module = module;
        this.writer = writer;
        this.project = writer.getProject();
        this.packagePath = module.getName() + File.separator +
                "src" + File.separator + "main" + File.separator + "java" + File.separator +
                project.getGroupId().replace(".", File.separator) + File.separator +
                project.getArtifactId().replace(".", File.separator) + File.separator;
    }

    public static void generate(Module module, ProjectWriter writer) {
        SpringBootGenerator generator = new SpringBootGenerator(module, writer);
        List<MysqlDataSource.Table> tables = generator.project.getDatasource().getMysql().getTables();

        generator.createPom();
        generator.createProperty();
        generator.createApplicationEntry();

        String postmanFlag = module.getProperty("postmanCollection");
        if (postmanFlag == null || "true".equals(postmanFlag)) {
            generator.createPostmanCollection(tables);
        }

        for (MysqlDataSource.Table table : tables) {
            generator.createController(table);
            generator.createEntity(table);

            String ormType = module.getProperty("ormType");
            if (StringUtils.isBlank(ormType)) {
                ormType = OrmType.MYBATIS_ANNOTATION.toString();
            }

            if (OrmType.MYBATIS_ANNOTATION.toString().equals(ormType)) {
                generator.createMybatisMapper(table);
                generator.createMybatisService(table);
                generator.createMybatisServiceImpl(table);
            } else if (OrmType.SPRING_DATA_JPA.toString().equals(ormType)) {
                generator.createJpaRepository(table);
                generator.createJpaService(table);
                generator.createJpaServiceImpl(table);
            } else {
                log.error("Unsupported ORM type: {}", module);
            }
        }

        // Load extensions
        for (Module extension : module.getExtensions()) {
            if (!extension.isEnable()) {
                continue;
            }
            switch (extension.getType()) {
                case ExtensionType.SPRING_BOOT_CROSS_ORIGIN:
                    SpringBootExtensionHandler.crossOrigin(module, writer);
                    break;
                case ExtensionType.SPRING_BOOT_LOMBOK:
                    SpringBootExtensionHandler.lombok(module, writer);
                    break;
                case ExtensionType.SPRING_BOOT_SWAGGER2:
                    SpringBootExtensionHandler.swagger2(module, writer);
                    break;
                default:
                    break;
            }
        }
    }

    public static void build(ProjectWriter writer) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(writer.getRootDirectory() + "pom.xml"));
        List<String> goals = new ArrayList<>();
        goals.add("compile");
        goals.add("package");
        request.setGoals(goals);
        Invoker invoker = new DefaultInvoker();
        String mavenHome = System.getenv("MAVEN_HOME");
        if (StringUtils.isBlank(mavenHome)) {
            log.error("Environment variable 'MAVEN_HOME' not set.");
            return;
        }
        invoker.setMavenHome(new File(mavenHome));
        try {
            invoker.execute(request);
        } catch (MavenInvocationException e) {
            e.printStackTrace();
        }
    }

    public void createPom() {
        PomFile pom = new PomFile(project);
        pom.addDependencyByLabel("default");
        if (module.getProperty("ormType").equals(OrmType.MYBATIS_ANNOTATION.toString())) {
            pom.addDependencyByLabel("mybatis");
        } else if (module.getProperty("ormType").equals(OrmType.SPRING_DATA_JPA.toString())) {
            pom.addDependencyByLabel("spring-data-jpa");
        }
        for (Module extension: module.getExtensions()) {
            pom.addDependencyByLabel(extension.getType());
        }
        writer.addOutput(TemplateType.SPRING_BOOT_POM, module.getName() + File.separator + "pom.xml", pom.toString());
    }

    public void createProperty() {
        PropertiesParser parser = new PropertiesParser();
        parser.add("spring.application.name", project.getArtifactId());

        String port = (module.getProperty("port") == null) ? "8080" : module.getProperty("port");
        parser.add("server.port", port);
        parser.addSeparator();

        // MySQL
        if (project.getDatasource().getMysql() != null) {
            MysqlDataSource.Connection connection = project.getDatasource().getMysql().getConnection();
            String datasourceUrl = "jdbc:mysql://" + connection.getHost() + ":" + connection.getPort() + "/" + connection.getDatabase();
            datasourceUrl += connection.getTimezone() != null ? "?serverTimezone=" + connection.getTimezone() : "";
            parser.add("spring.datasource.url", datasourceUrl);
            parser.add("spring.datasource.username", connection.getUsername());
            parser.add("spring.datasource.password", connection.getPassword());
        }

        writer.addOutput(TemplateType.SPRING_BOOT_PROPERTIES,
                module.getName() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties",
                parser.generate());
    }

    public void createApplicationEntry() {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_APPLICATION_ENTRY);
        TemplateFactory.preProcess(project, template);
        writer.addOutput(TemplateType.SPRING_BOOT_APPLICATION_ENTRY,
                packagePath + StrUtil.capitalize(project.getArtifactId()) + "Application.java",
                template);
        log.info("template: {}", template.toString());
    }

    public void createController(MysqlDataSource.Table table) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_CONTROLLER);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(table.getName()));
        template.replace("table", StrUtil.camelCase(table.getName()));
        template.replace("primaryKey", StrUtil.camelCase(table.getPrimaryKeyName()));

        if (module.getProperty("ormType").equals(OrmType.MYBATIS_ANNOTATION.toString())) {
            template.replace("pagition", "PageInfo");
            template.replace("pagitionImport", "com.github.pagehelper.PageInfo");
        } else if (module.getProperty("ormType").equals(OrmType.SPRING_DATA_JPA.toString())) {
            template.replace("pagition", "Page");
            template.replace("pagitionImport", "org.springframework.data.domain.Page");
        }

        writer.addOutput(TemplateType.SPRING_BOOT_CONTROLLER,
                packagePath + "controller" + File.separator + StrUtil.upperCamelCase(table.getName()) + "Controller.java",
                template);

    }

    public void createMybatisService(MysqlDataSource.Table table) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_SERVICE_MYBATIS);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(table.getName()));
        template.replace("table", StrUtil.camelCase(table.getName()));
        template.replace("primaryKey", StrUtil.camelCase(table.getPrimaryKeyName()));
        writer.addOutput(TemplateType.SPRING_BOOT_SERVICE_MYBATIS,
                packagePath + "service" + File.separator + StrUtil.upperCamelCase(table.getName()) + "Service.java",
                template);
    }

    public void createJpaService(MysqlDataSource.Table table) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_SERVICE_JPA);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(table.getName()));
        template.replace("table", StrUtil.camelCase(table.getName()));
        template.replace("primaryKey", StrUtil.camelCase(table.getPrimaryKeyName()));
        writer.addOutput(TemplateType.SPRING_BOOT_SERVICE_JPA,
                packagePath + "service" + File.separator + StrUtil.upperCamelCase(table.getName()) + "Service.java",
                template);
    }

    public void createMybatisServiceImpl(MysqlDataSource.Table table) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_SERVICE_IMPL_MYBATIS);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(table.getName()));
        template.replace("table", StrUtil.camelCase(table.getName()));
        template.replace("primaryKey", StrUtil.camelCase(table.getPrimaryKeyName()));
        writer.addOutput(TemplateType.SPRING_BOOT_SERVICE_IMPL_MYBATIS,
                packagePath + "service" + File.separator + "impl" + File.separator + StrUtil.upperCamelCase(table.getName()) + "ServiceImpl.java",
                template);
    }

    public void createJpaServiceImpl(MysqlDataSource.Table table) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_SERVICE_IMPL_JPA);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(table.getName()));
        template.replace("table", StrUtil.camelCase(table.getName()));
        template.replace("primaryKey", StrUtil.camelCase(table.getPrimaryKeyName()));
        writer.addOutput(TemplateType.SPRING_BOOT_SERVICE_IMPL_JPA,
                packagePath + "service" + File.separator + "impl" + File.separator + StrUtil.upperCamelCase(table.getName()) + "ServiceImpl.java",
                template);
    }

    public void createEntity(MysqlDataSource.Table table) {
        SpringBootDataClass entity = new SpringBootDataClass(project, table);

        // Attention: Adding Javadoc comment not works
        ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(entity.getCompilationUnit());
        if (clazz != null) {
            clazz.addOrphanComment(new JavadocComment("\r\n * @Author ExCode\r\n"));
            System.out.println(clazz);
        }
        writer.addOutput(TemplateType.SPRING_BOOT_ENTITY,
                packagePath + "entity" + File.separator + StrUtil.upperCamelCase(table.getName()) + ".java",
                entity.toString());
    }

    public void createMybatisMapper(MysqlDataSource.Table table) {
        if (StringUtils.isBlank(table.getPrimaryKeyName())) {
            log.error("Mapper interface cannot be generated from a table without a primary key");
            return;
        }

        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_MYBATIS_ANNOTATION_MAPPER);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(table.getName()));
        template.replace("table", StrUtil.camelCase(table.getName()));
        template.replace("primaryKey", StrUtil.camelCase(table.getPrimaryKeyName()));
        template.replace("primary_key", StrUtil.snakeCase(table.getPrimaryKeyName()));

        CompilationUnit unit = template.parseJavaSource();
        for (MethodDeclaration methodDeclaration : unit.getInterfaceByName(table.getUpperCamelCaseName() + "Mapper").get().getMethods()) {
            NodeList<AnnotationExpr> annotations = methodDeclaration.getAnnotations();
            for (AnnotationExpr annotationExpr : annotations) {
                if (annotationExpr.getNameAsString().equals("Insert")) {
                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
                            SqlUtil.insertQuery(table, true)));
                } else if (annotationExpr.getNameAsString().equals("Results")) {
                    ArrayInitializerExpr array = new ArrayInitializerExpr();
                    for (MysqlDataSource.Table.Column column : table.getColumns()) {
                        if (methodDeclaration.getNameAsString().equals("retrieveList")) {
//                            if (column.isDetail()) {
//                                continue;
//                            }
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
                            SqlUtil.updateQuery(table)));
                }
            }
        }
        template.updateJavaSource(unit);
        writer.addOutput(TemplateType.SPRING_BOOT_MYBATIS_ANNOTATION_MAPPER,
                packagePath + "mapper" + File.separator + StrUtil.upperCamelCase(table.getName()) + "Mapper.java",
                template);
    }

    public void createJpaRepository(MysqlDataSource.Table table) {
        if (StringUtils.isBlank(table.getPrimaryKeyName())) {
            log.error("Repository interface cannot be generated from a table without a primary key");
            return;
        }

        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_JPA_REPOSITORY);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(table.getName()));
        template.replace("table", StrUtil.camelCase(table.getName()));
        template.replace("primaryKey", StrUtil.camelCase(table.getPrimaryKeyName()));
        template.replace("primary_key", StrUtil.snakeCase(table.getPrimaryKeyName()));

        writer.addOutput(TemplateType.SPRING_BOOT_JPA_REPOSITORY,
                packagePath + "repository" + File.separator + StrUtil.upperCamelCase(table.getName()) + "Repository.java",
                template);
    }

    public void createPostmanCollection(List<MysqlDataSource.Table> tables) {
        PostmanCollection collection = new PostmanCollection();

        PostmanCollection.Info info = collection.getInfo();
        info.setPostmanId("");
        info.setName(project.getArtifactId());

        List<PostmanCollection.Folder> folders = collection.getFolders();
        for (MysqlDataSource.Table table: tables) {
            PostmanCollection.Folder folder = new PostmanCollection.Folder();
            folder.setName(table.getName());
            folders.add(folder);

            String[] verbs = new String[] { "Query", "Create", "Update", "Delete" };
            String[] methods = new String[] { "GET", "POST", "PUT", "DELETE" };

            String port = module.getProperty("port");
            if (StringUtils.isBlank(port)) {
                port = "8080";
            }

            for (int i = 0; i < methods.length; i ++) {
                PostmanCollection.Folder.Api api = new PostmanCollection.Folder.Api();
                folder.getApis().add(api);
                api.setName( verbs[i] + " " + table.getName());
                api.getRequest().setMethod(methods[i]);

                PostmanCollection.Folder.Api.Url url = api.getRequest().getUrl();
                url.setRaw("http://localhost:" + port + "/api/" + project.getArtifactId() + "/" + table.getName());
                url.setProtocol("http");
                url.setPort(port);
                url.setHost(Arrays.asList("localhost"));
                url.setPath(Arrays.asList("api", project.getArtifactId(), table.getName()));
            }
        }
        writer.addOutput(TemplateType.RAW_FILE, project.getArtifactId() + ".postman_collection.json", collection.toJson());
    }
}
