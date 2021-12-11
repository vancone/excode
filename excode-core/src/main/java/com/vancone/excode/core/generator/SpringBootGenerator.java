package com.vancone.excode.core.generator;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.vancone.excode.core.ProjectWriter;
import com.vancone.excode.core.PropertiesParser;
import com.vancone.excode.core.TemplateFactory;
import com.vancone.excode.core.constant.ExtensionType;
import com.vancone.excode.core.enums.DataCarrier;
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
import java.util.Optional;

/**
 * @author Tenton Lien
 * @since 7/24/2021
 */
@Slf4j
public class SpringBootGenerator {

    private String packagePath;
    private ProjectWriter writer;
    private Project project;
    private Project.DataAccess.Solution.JavaSpringBoot module;

    private SpringBootGenerator(Project.DataAccess.Solution.JavaSpringBoot module, ProjectWriter writer) {
        this.module = module;
        this.writer = writer;
        this.project = writer.getProject();
        this.packagePath = project.getName() + File.separator +
                "src" + File.separator + "main" + File.separator + "java" + File.separator +
                module.getGroupId().replace(".", File.separator) + File.separator +
                module.getArtifactId().replace(".", File.separator).replace('-', File.separatorChar) + File.separator;
    }

    public static void generate(Project.DataAccess.Solution.JavaSpringBoot module, ProjectWriter writer) {
        SpringBootGenerator generator = new SpringBootGenerator(module, writer);
        List<DataStore> stores = generator.project.getDataAccess().getDataStores();

        generator.createPom();
        generator.createProperty();
        generator.createApplicationEntry();

//        String postmanFlag = module.getProperty("postmanCollection");
//        if (postmanFlag == null || "true".equals(postmanFlag)) {
//            generator.createPostmanCollection(stores);
//        }

        for (DataStore store : stores) {
            if (store.getCarrier() != DataCarrier.MYSQL) {
                continue;
            }
            generator.createController(store);
            generator.createEntity(store);

            OrmType ormType = module.getOrmType();

            if (ormType == OrmType.MYBATIS_ANNOTATION) {
                generator.createMybatisMapper(store);
                generator.createMybatisService(store);
                generator.createMybatisServiceImpl(store);
            } else if (ormType == OrmType.SPRING_DATA_JPA) {
                generator.createJpaRepository(store);
                generator.createJpaService(store);
                generator.createJpaServiceImpl(store);
            } else {
                log.error("Unsupported ORM type: {}", module);
            }
        }

        // Load extensions
        for (Project.DataAccess.Solution.JavaSpringBoot.Extension extension : module.getExtensions()) {
            if (!extension.isEnable()) {
                continue;
            }
            switch (extension.getId()) {
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
        if (module.getOrmType() == OrmType.MYBATIS_ANNOTATION) {
            pom.addDependencyByLabel("mybatis");
        } else if (module.getOrmType() == OrmType.SPRING_DATA_JPA) {
            pom.addDependencyByLabel("spring-data-jpa");
        }
        for (Project.DataAccess.Solution.JavaSpringBoot.Extension extension: module.getExtensions()) {
            pom.addDependencyByLabel(extension.getId());
        }
        writer.addOutput(TemplateType.SPRING_BOOT_POM, project.getName() + File.separator + "pom.xml", pom.toString());
    }

    public void createProperty() {
        PropertiesParser parser = new PropertiesParser();
        parser.add("spring.application.name", module.getArtifactId());

        String port = (module.getServerPort() == null) ? "8080" : module.getServerPort().toString();
        parser.add("server.port", port);
        parser.addSeparator();

        // MySQL
        if (!project.getDataAccess().getDataStoreByCarrier(DataCarrier.MYSQL).isEmpty()) {
            DataStore.Connection connection = project.getDataAccess().getDataStoreByCarrier(DataCarrier.MYSQL).get(0).getConnection();
//            MysqlDataSource.Connection connection = project.getDatasource().getMysql().getConnection();
            String datasourceUrl = "jdbc:mysql://" + connection.getHost() + ":" + connection.getPort() + "/" + connection.getDatabase();
            datasourceUrl += connection.getTimezone() != null ? "?serverTimezone=" + connection.getTimezone() : "";
            parser.add("spring.datasource.url", datasourceUrl);
            parser.add("spring.datasource.username", connection.getUsername());
            parser.add("spring.datasource.password", connection.getPassword());
        }

        writer.addOutput(TemplateType.SPRING_BOOT_PROPERTIES,
                project.getName() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties",
                parser.generate());
    }

    public void createApplicationEntry() {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_APPLICATION_ENTRY);
        TemplateFactory.preProcess(project, template);
        writer.addOutput(TemplateType.SPRING_BOOT_APPLICATION_ENTRY,
                packagePath + StrUtil.upperCamelCase(module.getArtifactId()) + "Application.java",
                template);
    }

    public void createController(DataStore store) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_CONTROLLER);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(store.getName()));
        template.replace("table", StrUtil.camelCase(store.getName()));
        template.replace("primaryKey", StrUtil.camelCase(store.getNodes().get(0).getName()));

        if (module.getOrmType() == OrmType.MYBATIS_ANNOTATION) {
            template.replace("pagition", "PageInfo");
            template.replace("pagitionImport", "com.github.pagehelper.PageInfo");
        } else if (module.getOrmType() == OrmType.SPRING_DATA_JPA) {
            template.replace("pagition", "Page");
            template.replace("pagitionImport", "org.springframework.data.domain.Page");
        }

        for (DataStore.Node node: store.getNodes()) {
            String filterMode = ""; // node.getFilter();
            if (StringUtils.isNotBlank(filterMode)) {
                CompilationUnit unit = template.parseJavaSource();
                ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);
                List<MethodDeclaration> methods = clazz.getMethodsByName("retrievePage");
                if (!methods.isEmpty()) {
                    MethodDeclaration method = methods.get(0);

                    // Add parameters
                    NodeList<Parameter> params = method.getParameters();
                    Parameter param = new Parameter();
                    param.setName(node.getName());
                    param.setType("String");
                    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
                    annotation.setName("RequestParam");
                    annotation.addPair("required", "false");
                    param.addAnnotation(annotation);
                    params.add(param);
                    log.info("Params: {}", params);

                    // Add args of method call
                    Optional<BlockStmt> body = method.getBody();
                    if (body.isPresent()) {
                        Statement statement = body.get().getStatement(0);
                        List<Node> nodes = statement.getChildNodes();
                        if (!nodes.isEmpty()) {
                            VariableDeclarationExpr expr = (VariableDeclarationExpr) nodes.get(0);
                            Optional<Expression> initializer = expr.getVariable(0).getInitializer();
                            if (initializer.isPresent()) {
                                NodeList<Expression> args = initializer.get().asMethodCallExpr().getArguments();
                                args.add(new NameExpr((node.getName())));
                            }
                        }
                    }
                }
                template.updateJavaSource(unit);
            }
        }

        writer.addOutput(TemplateType.SPRING_BOOT_CONTROLLER,
                packagePath + "controller" + File.separator + StrUtil.upperCamelCase(store.getName()) + "Controller.java",
                template);

    }

    public void createMybatisService(DataStore store) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_SERVICE_MYBATIS);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(store.getName()));
        template.replace("table", StrUtil.camelCase(store.getName()));
        template.replace("primaryKey", StrUtil.camelCase(store.getPrimaryKeyName()));

        for (DataStore.Node node: store.getNodes()) {
            String filterMode = ""; // node.getFilter();
            if (StringUtils.isNotBlank(filterMode)) {
                CompilationUnit unit = template.parseJavaSource();
                ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);
                List<MethodDeclaration> methods = clazz.getMethodsByName("retrievePage");
                if (!methods.isEmpty()) {
                    MethodDeclaration method = methods.get(0);

                    // Add parameters
                    NodeList<Parameter> params = method.getParameters();
                    Parameter param = new Parameter();
                    param.setName(node.getName());
                    param.setType("String");
                    params.add(param);
                }
                template.updateJavaSource(unit);
            }
        }

        writer.addOutput(TemplateType.SPRING_BOOT_SERVICE_MYBATIS,
                packagePath + "service" + File.separator + StrUtil.upperCamelCase(store.getName()) + "Service.java",
                template);
    }

    public void createJpaService(DataStore store) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_SERVICE_JPA);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(store.getName()));
        template.replace("table", StrUtil.camelCase(store.getName()));
        template.replace("primaryKey", StrUtil.camelCase(store.getPrimaryKeyName()));
        writer.addOutput(TemplateType.SPRING_BOOT_SERVICE_JPA,
                packagePath + "service" + File.separator + StrUtil.upperCamelCase(store.getName()) + "Service.java",
                template);
    }

    public void createMybatisServiceImpl(DataStore store) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_SERVICE_IMPL_MYBATIS);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(store.getName()));
        template.replace("table", StrUtil.camelCase(store.getName()));
        template.replace("primaryKey", StrUtil.camelCase(store.getPrimaryKeyName()));

        for (DataStore.Node node: store.getNodes()) {
            String filterMode = ""; // node.getFilter();
            if (StringUtils.isNotBlank(filterMode)) {
                CompilationUnit unit = template.parseJavaSource();
                ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);
                List<MethodDeclaration> methods = clazz.getMethodsByName("retrievePage");
                if (!methods.isEmpty()) {
                    MethodDeclaration method = methods.get(0);

                    // Add parameters
                    NodeList<Parameter> params = method.getParameters();
                    Parameter param = new Parameter();
                    param.setName(node.getName());
                    param.setType("String");
                    params.add(param);
                }
                template.updateJavaSource(unit);
            }
        }

        writer.addOutput(TemplateType.SPRING_BOOT_SERVICE_IMPL_MYBATIS,
                packagePath + "service" + File.separator + "impl" + File.separator + StrUtil.upperCamelCase(store.getName()) + "ServiceImpl.java",
                template);
    }

    public void createJpaServiceImpl(DataStore store) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_SERVICE_IMPL_JPA);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(store.getName()));
        template.replace("table", StrUtil.camelCase(store.getName()));
        template.replace("primaryKey", StrUtil.camelCase(store.getPrimaryKeyName()));
        writer.addOutput(TemplateType.SPRING_BOOT_SERVICE_IMPL_JPA,
                packagePath + "service" + File.separator + "impl" + File.separator + StrUtil.upperCamelCase(store.getName()) + "ServiceImpl.java",
                template);
    }

    public void createEntity(DataStore store) {
        SpringBootDataClass entity = new SpringBootDataClass(project, store);

        // Attention: Adding Javadoc comment not works
        ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(entity.getCompilationUnit());
        if (clazz != null) {
            clazz.addOrphanComment(new JavadocComment("\r\n * @Author ExCode\r\n"));
        }
        writer.addOutput(TemplateType.SPRING_BOOT_ENTITY,
                packagePath + "entity" + File.separator + StrUtil.upperCamelCase(store.getName()) + ".java",
                entity.toString());
    }

    public void createMybatisMapper(DataStore store) {
        if (StringUtils.isBlank(store.getNodes().get(0).getName())) {
            log.error("Mapper interface cannot be generated from a table without a primary key");
            return;
        }

        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_MYBATIS_ANNOTATION_MAPPER);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(store.getName()));
        template.replace("table", StrUtil.camelCase(store.getName()));
        template.replace("primaryKey", StrUtil.camelCase(store.getNodes().get(0).getName()));
        template.replace("primary_key", StrUtil.snakeCase(store.getNodes().get(0).getName()));

        CompilationUnit unit = template.parseJavaSource();
        for (MethodDeclaration methodDeclaration : unit.getInterfaceByName(StrUtil.upperCamelCase(store.getName()) + "Mapper").get().getMethods()) {
            NodeList<AnnotationExpr> annotations = methodDeclaration.getAnnotations();
            for (AnnotationExpr annotationExpr : annotations) {
                if (annotationExpr.getNameAsString().equals("Insert")) {
                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
                            SqlUtil.insertQuery(store, true)));
                } else if (annotationExpr.getNameAsString().equals("Results")) {
                    ArrayInitializerExpr array = new ArrayInitializerExpr();
                    for (DataStore.Node node : store.getNodes()) {
                        NormalAnnotationExpr resultAnnotation = new NormalAnnotationExpr();
                        resultAnnotation.setName("Result");
                        resultAnnotation.addPair("property", new StringLiteralExpr(node.getName()));
                        resultAnnotation.addPair("column", new StringLiteralExpr(node.getName()));
                        array.getValues().add(resultAnnotation);
                    }
                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(array);
                } else if (annotationExpr.getNameAsString().equals("Update")) {
                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
                            SqlUtil.updateQuery(store)));
                }
            }
        }
        template.updateJavaSource(unit);
        writer.addOutput(TemplateType.SPRING_BOOT_MYBATIS_ANNOTATION_MAPPER,
                packagePath + "mapper" + File.separator + StrUtil.upperCamelCase(store.getName()) + "Mapper.java",
                template);
    }

    public void createJpaRepository(DataStore store) {
        if (StringUtils.isBlank(store.getPrimaryKeyName())) {
            log.error("Repository interface cannot be generated from a table without a primary key");
            return;
        }

        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_JPA_REPOSITORY);
        TemplateFactory.preProcess(project, template);
        template.replace("Table", StrUtil.upperCamelCase(store.getName()));
        template.replace("table", StrUtil.camelCase(store.getName()));
        template.replace("primaryKey", StrUtil.camelCase(store.getPrimaryKeyName()));
        template.replace("primary_key", StrUtil.snakeCase(store.getPrimaryKeyName()));

        writer.addOutput(TemplateType.SPRING_BOOT_JPA_REPOSITORY,
                packagePath + "repository" + File.separator + StrUtil.upperCamelCase(store.getName()) + "Repository.java",
                template);
    }

    public void createPostmanCollection(List<MysqlDataSource.Table> tables) {
        PostmanCollection collection = new PostmanCollection();

        PostmanCollection.Info info = collection.getInfo();
        info.setPostmanId("");
        info.setName(module.getArtifactId());

        List<PostmanCollection.Folder> folders = collection.getFolders();
        for (MysqlDataSource.Table table: tables) {
            PostmanCollection.Folder folder = new PostmanCollection.Folder();
            folder.setName(table.getName());
            folders.add(folder);

            String[] verbs = new String[] { "Query", "Create", "Update", "Delete" };
            String[] methods = new String[] { "GET", "POST", "PUT", "DELETE" };

            String port = module.getServerPort().toString();
            if (StringUtils.isBlank(port)) {
                port = "8080";
            }

            for (int i = 0; i < methods.length; i ++) {
                PostmanCollection.Folder.Api api = new PostmanCollection.Folder.Api();
                folder.getApis().add(api);
                api.setName( verbs[i] + " " + table.getName());
                api.getRequest().setMethod(methods[i]);

                PostmanCollection.Folder.Api.Url url = api.getRequest().getUrl();
                url.setRaw("http://localhost:" + port + "/api/" + module.getArtifactId() + "/" + table.getName());
                url.setProtocol("http");
                url.setPort(port);
                url.setHost(Arrays.asList("localhost"));
                url.setPath(Arrays.asList("api", module.getArtifactId(), table.getName()));
            }
        }
        writer.addOutput(TemplateType.RAW_FILE, module.getArtifactId() + ".postman_collection.json", collection.toJson());
    }
}
