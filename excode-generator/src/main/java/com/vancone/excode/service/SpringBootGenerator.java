//package com.vancone.excode.service;
//
//import com.github.javaparser.ast.CompilationUnit;
//import com.github.javaparser.ast.Node;
//import com.github.javaparser.ast.NodeList;
//import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
//import com.github.javaparser.ast.body.MethodDeclaration;
//import com.github.javaparser.ast.body.Parameter;
//import com.github.javaparser.ast.comments.JavadocComment;
//import com.github.javaparser.ast.expr.*;
//import com.github.javaparser.ast.stmt.BlockStmt;
//import com.github.javaparser.ast.stmt.Statement;
//import com.vancone.excode.entity.*;
//import com.vancone.excode.entity.microservice.ApplicationProperties;
//import com.vancone.excode.entity.microservice.ProjectObjectModel;
//import com.vancone.excode.entity.microservice.SpringBootDataClass;
//import com.vancone.excode.enums.TemplateType;
//import com.vancone.excode.service.microservice.ApplicationPropertiesService;
//import com.vancone.excode.constant.ExtensionType;
////import com.vancone.excode.generator.entity.*;
//import com.vancone.excode.entity.datasource.MysqlDataSource;
//import com.vancone.excode.enums.DataCarrier;
//import com.vancone.excode.enums.OrmType;
//import com.vancone.excode.extension.SpringBootExtensionHandler;
//import com.vancone.excode.util.CompilationUnitUtil;
//import com.vancone.excode.util.SqlUtil;
//import com.vancone.excode.util.StrUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.maven.shared.invoker.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.data.mongodb.core.query.Criteria;
//import org.springframework.data.mongodb.core.query.Query;
//import org.springframework.stereotype.Service;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
///**
// * @author Tenton Lien
// * @since 2021/07/24
// */
//@Slf4j
//@Service
//public class SpringBootGenerator {
//
//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//    @Autowired
//    private ProgressLogService progressLogService;
//
//    @Autowired
//    private TemplateService templateService;
//
//    @Autowired
//    private SpringBootExtensionHandler springBootExtensionHandler;
//
//    public List<Output> generate(Project project) {
//        Project.DataAccess.Solution.JavaSpringBoot module = project.getDataAccess().getSolution().getJavaSpringBoot();
//        List<Output> outputs = new ArrayList<>();
//        List<DataStore> stores = project.getDataAccess().getDataStores();
//
//        module.setPackagePath(module.getName() + File.separator +
//                "src" + File.separator + "main" + File.separator + "java" + File.separator +
//                module.getGroupId().replace(".", File.separator) + File.separator +
//                module.getArtifactId().replace(".", File.separator).replace('-', File.separatorChar) + File.separator);
//
//        outputs.add(createPom(project));
//        outputs.add(createProperty(project));
//        outputs.add(createApplicationEntry(module, module.getArtifactId()));
//
//        for (DataStore store : stores) {
//            if (store.getCarrier() != DataCarrier.MYSQL) {
//                continue;
//            }
//            outputs.add(createController(project, store));
//            progressLogService.output(project.getId(), "=> Entity Class (" + store.getName() + ")");
//            outputs.add(createEntity(module, store));
//
//            OrmType ormType = module.getOrmType();
//
//            if (ormType == OrmType.MYBATIS_ANNOTATION) {
//                outputs.add(createMybatisMapper(module, store));
//                outputs.add(createMybatisService(module, store));
//                outputs.add(createMybatisServiceImpl(module, store));
//            } else if (ormType == OrmType.SPRING_DATA_JPA) {
//                outputs.add(createJpaRepository(module, store));
//                outputs.add(createJpaService(module, store));
//                outputs.add(createJpaServiceImpl(module, store));
//            } else {
//                log.error("Unsupported ORM type: {}", module);
//            }
//        }
//
//        // Load extensions
//        for (Project.DataAccess.Solution.JavaSpringBoot.Extension extension : module.getExtensions()) {
//            if (!extension.isEnable()) {
//                continue;
//            }
//            switch (extension.getId()) {
//                case ExtensionType.SPRING_BOOT_CROSS_ORIGIN:
//                    outputs.add(springBootExtensionHandler.crossOrigin(module));
//                    break;
//                case ExtensionType.SPRING_BOOT_LOMBOK:
//                    springBootExtensionHandler.lombok(outputs);
//                    break;
//                case ExtensionType.SPRING_BOOT_SWAGGER2:
//                    outputs.add(springBootExtensionHandler.swagger2(module, project.getDataAccess(), outputs));
//                    break;
//                default:
//                    break;
//            }
//        }
//        return outputs;
//    }
//
//    public void build(String rootDirectory) {
//        InvocationRequest request = new DefaultInvocationRequest();
//        request.setPomFile(new File(rootDirectory + "pom.xml"));
//        List<String> goals = new ArrayList<>();
//        goals.add("compile");
//        goals.add("package");
//        request.setGoals(goals);
//        Invoker invoker = new DefaultInvoker();
//        String mavenHome = System.getenv("MAVEN_HOME");
//        if (StringUtils.isBlank(mavenHome)) {
//            log.error("Environment variable 'MAVEN_HOME' not set.");
//            return;
//        }
//        invoker.setMavenHome(new File(mavenHome));
//        try {
//            invoker.execute(request);
//        } catch (MavenInvocationException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public Output createPom(Project project) {
//        Project.DataAccess.Solution.JavaSpringBoot module = project.getDataAccess().getSolution().getJavaSpringBoot();
//        progressLogService.output(project.getId(), "=> Project Model Object");
//        ProjectObjectModel pom = new ProjectObjectModel(project);
//        addDependencyByLabel(pom, "default");
//        if (module.getOrmType() == OrmType.MYBATIS_ANNOTATION) {
//            addDependencyByLabel(pom, "mybatis");
//        } else if (module.getOrmType() == OrmType.SPRING_DATA_JPA) {
//            addDependencyByLabel(pom, "spring-data-jpa");
//        }
//        for (Project.DataAccess.Solution.JavaSpringBoot.Extension extension: module.getExtensions()) {
//            addDependencyByLabel(pom, extension.getId());
//        }
//        return new Output(TemplateType.SPRING_BOOT_POM, module.getName() + File.separator + "pom.xml", pom.toString());
//    }
//
//    public void addDependencyByLabel(ProjectObjectModel pom, String label) {
//        List<ProjectObjectModel.Dependency> dependencies = mongoTemplate.find(Query.query(Criteria.where("label").is(label)), ProjectObjectModel.Dependency.class);
//        if (!dependencies.isEmpty()) {
//            pom.getDependencies().addAll(dependencies);
//        }
//    }
//
//    public Output createProperty(Project project) {
//        Project.DataAccess.Solution.JavaSpringBoot module = project.getDataAccess().getSolution().getJavaSpringBoot();
//        progressLogService.output(project.getId(), "=> Properties");
//        ApplicationPropertiesService parser = new ApplicationPropertiesService();
//        ApplicationProperties properties = new ApplicationProperties();
//        properties.add("spring.application.name", module.getArtifactId());
//
//        String port = (module.getServerPort() == null) ? "8080" : module.getServerPort().toString();
//        properties.add("server.port", port);
//        properties.addSeparator();
//
//        // MySQL
//        if (!project.getDataAccess().getDataStoreByCarrier(DataCarrier.MYSQL).isEmpty()) {
//            DataStore.Connection connection = project.getDataAccess().getDataStoreByCarrier(DataCarrier.MYSQL).get(0).getConnection();
////            MysqlDataSource.Connection connection = project.getDatasource().getMysql().getConnection();
//            String datasourceUrl = "jdbc:mysql://" + connection.getHost() + ":" + connection.getPort() + "/" + connection.getDatabase();
//            datasourceUrl += connection.getTimezone() != null ? "?serverTimezone=" + connection.getTimezone() : "";
//            properties.add("spring.datasource.url", datasourceUrl);
//            properties.add("spring.datasource.username", connection.getUsername());
//            properties.add("spring.datasource.password", connection.getPassword());
//        }
//
//        return new Output(TemplateType.SPRING_BOOT_PROPERTIES,
//                module.getName() + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "application.properties",
//                parser.generate(properties));
//    }
//
//    public Output createApplicationEntry(Project.DataAccess.Solution.JavaSpringBoot module, String artifactId) {
//        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_APPLICATION_ENTRY);
//        templateService.preProcess(module, template);
//        return new Output(TemplateType.SPRING_BOOT_APPLICATION_ENTRY,
//                module.getPackagePath() + StrUtil.toPascalCase(artifactId) + "Application.java",
//                template);
//    }
//
//    public Output createController(Project project, DataStore store) {
//        Project.DataAccess.Solution.JavaSpringBoot module = project.getDataAccess().getSolution().getJavaSpringBoot();
//        progressLogService.output(project.getId(), "=> Controller (" + store.getName() + ")");
//        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_CONTROLLER);
//        templateService.preProcess(module, template);
//        template.replace("Table", StrUtil.toPascalCase(store.getName()));
//        template.replace("table", StrUtil.toCamelCase(store.getName()));
//        template.replace("primaryKey", StrUtil.toCamelCase(store.getNodes().get(0).getName()));
//
//        if (module.getOrmType() == OrmType.MYBATIS_ANNOTATION) {
//            template.replace("pagition", "PageInfo");
//            template.replace("pagitionImport", "com.github.pagehelper.PageInfo");
//        } else if (module.getOrmType() == OrmType.SPRING_DATA_JPA) {
//            template.replace("pagition", "Page");
//            template.replace("pagitionImport", "org.springframework.data.domain.Page");
//        }
//
//        for (DataStore.Node node: store.getNodes()) {
//            String filterMode = ""; // node.getFilter();
//            if (StringUtils.isNotBlank(filterMode)) {
//                CompilationUnit unit = template.parseJavaSource();
//                ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);
//                List<MethodDeclaration> methods = clazz.getMethodsByName("retrievePage");
//                if (!methods.isEmpty()) {
//                    MethodDeclaration method = methods.get(0);
//
//                    // Add parameters
//                    NodeList<Parameter> params = method.getParameters();
//                    Parameter param = new Parameter();
//                    param.setName(node.getName());
//                    param.setType("String");
//                    NormalAnnotationExpr annotation = new NormalAnnotationExpr();
//                    annotation.setName("RequestParam");
//                    annotation.addPair("required", "false");
//                    param.addAnnotation(annotation);
//                    params.add(param);
//                    log.info("Params: {}", params);
//
//                    // Add args of method call
//                    Optional<BlockStmt> body = method.getBody();
//                    if (body.isPresent()) {
//                        Statement statement = body.get().getStatement(0);
//                        List<Node> nodes = statement.getChildNodes();
//                        if (!nodes.isEmpty()) {
//                            VariableDeclarationExpr expr = (VariableDeclarationExpr) nodes.get(0);
//                            Optional<Expression> initializer = expr.getVariable(0).getInitializer();
//                            if (initializer.isPresent()) {
//                                NodeList<Expression> args = initializer.get().asMethodCallExpr().getArguments();
//                                args.add(new NameExpr((node.getName())));
//                            }
//                        }
//                    }
//                }
//                template.updateJavaSource(unit);
//            }
//        }
//
//        return new Output(TemplateType.SPRING_BOOT_CONTROLLER,
//                module.getPackagePath() + "controller" + File.separator + StrUtil.toPascalCase(store.getName()) + "Controller.java",
//                template);
//
//    }
//
//    public Output createMybatisService(Project.DataAccess.Solution.JavaSpringBoot module, DataStore store) {
//        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_SERVICE_MYBATIS);
//        templateService.preProcess(module, template);
//        template.replace("Table", StrUtil.toPascalCase(store.getName()));
//        template.replace("table", StrUtil.toCamelCase(store.getName()));
//        template.replace("primaryKey", StrUtil.toCamelCase(store.getPrimaryKeyName()));
//
//        for (DataStore.Node node: store.getNodes()) {
//            String filterMode = ""; // node.getFilter();
//            if (StringUtils.isNotBlank(filterMode)) {
//                CompilationUnit unit = template.parseJavaSource();
//                ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);
//                List<MethodDeclaration> methods = clazz.getMethodsByName("retrievePage");
//                if (!methods.isEmpty()) {
//                    MethodDeclaration method = methods.get(0);
//
//                    // Add parameters
//                    NodeList<Parameter> params = method.getParameters();
//                    Parameter param = new Parameter();
//                    param.setName(node.getName());
//                    param.setType("String");
//                    params.add(param);
//                }
//                template.updateJavaSource(unit);
//            }
//        }
//
//        return new Output(TemplateType.SPRING_BOOT_SERVICE_MYBATIS,
//                module.getPackagePath() + "service" + File.separator + StrUtil.toPascalCase(store.getName()) + "Service.java",
//                template);
//    }
//
//    public Output createJpaService(Project.DataAccess.Solution.JavaSpringBoot module, DataStore store) {
//        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_SERVICE_JPA);
//        templateService.preProcess(module, template);
//        template.replace("Table", StrUtil.toPascalCase(store.getName()));
//        template.replace("table", StrUtil.toCamelCase(store.getName()));
//        template.replace("primaryKey", StrUtil.toCamelCase(store.getPrimaryKeyName()));
//        return new Output(TemplateType.SPRING_BOOT_SERVICE_JPA,
//                module.getPackagePath() + "service" + File.separator + StrUtil.toPascalCase(store.getName()) + "Service.java",
//                template);
//    }
//
//    public Output createMybatisServiceImpl(Project.DataAccess.Solution.JavaSpringBoot module, DataStore store) {
//        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_SERVICE_IMPL_MYBATIS);
//        templateService.preProcess(module, template);
//        template.replace("Table", StrUtil.toPascalCase(store.getName()));
//        template.replace("table", StrUtil.toCamelCase(store.getName()));
//        template.replace("primaryKey", StrUtil.toCamelCase(store.getPrimaryKeyName()));
//
//        for (DataStore.Node node: store.getNodes()) {
//            String filterMode = ""; // node.getFilter();
//            if (StringUtils.isNotBlank(filterMode)) {
//                CompilationUnit unit = template.parseJavaSource();
//                ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);
//                List<MethodDeclaration> methods = clazz.getMethodsByName("retrievePage");
//                if (!methods.isEmpty()) {
//                    MethodDeclaration method = methods.get(0);
//
//                    // Add parameters
//                    NodeList<Parameter> params = method.getParameters();
//                    Parameter param = new Parameter();
//                    param.setName(node.getName());
//                    param.setType("String");
//                    params.add(param);
//                }
//                template.updateJavaSource(unit);
//            }
//        }
//
//        return new Output(TemplateType.SPRING_BOOT_SERVICE_IMPL_MYBATIS,
//                module.getPackagePath() + "service" + File.separator + "impl" + File.separator + StrUtil.toPascalCase(store.getName()) + "ServiceImpl.java",
//                template);
//    }
//
//    public Output createJpaServiceImpl(Project.DataAccess.Solution.JavaSpringBoot module, DataStore store) {
//        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_SERVICE_IMPL_JPA);
//        templateService.preProcess(module, template);
//        template.replace("Table", StrUtil.toPascalCase(store.getName()));
//        template.replace("table", StrUtil.toCamelCase(store.getName()));
//        template.replace("primaryKey", StrUtil.toCamelCase(store.getPrimaryKeyName()));
//        return new Output(TemplateType.SPRING_BOOT_SERVICE_IMPL_JPA,
//                module.getPackagePath() + "service" + File.separator + "impl" + File.separator + StrUtil.toPascalCase(store.getName()) + "ServiceImpl.java",
//                template);
//    }
//
//    public Output createEntity(Project.DataAccess.Solution.JavaSpringBoot module, DataStore store) {
//        SpringBootDataClass entity = new SpringBootDataClass(module, store);
//
//        // Attention: Adding Javadoc comment not works
//        ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(entity.getCompilationUnit());
//        if (clazz != null) {
//            clazz.addOrphanComment(new JavadocComment("\r\n * @Author ExCode\r\n"));
//        }
//        return new Output(TemplateType.SPRING_BOOT_ENTITY,
//                module.getPackagePath() + "entity" + File.separator + StrUtil.toPascalCase(store.getName()) + ".java",
//                entity.toString());
//    }
//
//    public Output createMybatisMapper(Project.DataAccess.Solution.JavaSpringBoot module, DataStore store) {
//        if (StringUtils.isBlank(store.getNodes().get(0).getName())) {
//            log.error("Mapper interface cannot be generated from a table without a primary key");
//            return null;
//        }
//
//        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_MYBATIS_ANNOTATION_MAPPER);
//        templateService.preProcess(module, template);
//        template.replace("Table", StrUtil.toPascalCase(store.getName()));
//        template.replace("table", StrUtil.toCamelCase(store.getName()));
//        template.replace("primaryKey", StrUtil.toCamelCase(store.getNodes().get(0).getName()));
//        template.replace("primary_key", StrUtil.toSnakeCase(store.getNodes().get(0).getName()));
//
//        CompilationUnit unit = template.parseJavaSource();
//        for (MethodDeclaration methodDeclaration : unit.getInterfaceByName(StrUtil.toPascalCase(store.getName()) + "Mapper").get().getMethods()) {
//            NodeList<AnnotationExpr> annotations = methodDeclaration.getAnnotations();
//            for (AnnotationExpr annotationExpr : annotations) {
//                if (annotationExpr.getNameAsString().equals("Insert")) {
//                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
//                            SqlUtil.insertQuery(store, true)));
//                } else if (annotationExpr.getNameAsString().equals("Results")) {
//                    ArrayInitializerExpr array = new ArrayInitializerExpr();
//                    for (DataStore.Node node : store.getNodes()) {
//                        NormalAnnotationExpr resultAnnotation = new NormalAnnotationExpr();
//                        resultAnnotation.setName("Result");
//                        resultAnnotation.addPair("property", new StringLiteralExpr(node.getName()));
//                        resultAnnotation.addPair("column", new StringLiteralExpr(node.getName()));
//                        array.getValues().add(resultAnnotation);
//                    }
//                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(array);
//                } else if (annotationExpr.getNameAsString().equals("Update")) {
//                    annotationExpr.asSingleMemberAnnotationExpr().setMemberValue(new StringLiteralExpr(
//                            SqlUtil.updateQuery(store)));
//                }
//            }
//        }
//        template.updateJavaSource(unit);
//        return new Output(TemplateType.SPRING_BOOT_MYBATIS_ANNOTATION_MAPPER,
//                module.getPackagePath() + "mapper" + File.separator + StrUtil.toPascalCase(store.getName()) + "Mapper.java",
//                template);
//    }
//
//    public Output createJpaRepository(Project.DataAccess.Solution.JavaSpringBoot module, DataStore store) {
//        if (StringUtils.isBlank(store.getPrimaryKeyName())) {
//            log.error("Repository interface cannot be generated from a table without a primary key");
//            return null;
//        }
//
//        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_JPA_REPOSITORY);
//        templateService.preProcess(module, template);
//        template.replace("Table", StrUtil.toPascalCase(store.getName()));
//        template.replace("table", StrUtil.toCamelCase(store.getName()));
//        template.replace("primaryKey", StrUtil.toCamelCase(store.getPrimaryKeyName()));
//        template.replace("primary_key", StrUtil.toSnakeCase(store.getPrimaryKeyName()));
//
//        return new Output(TemplateType.SPRING_BOOT_JPA_REPOSITORY,
//                module.getPackagePath() + "repository" + File.separator + StrUtil.toPascalCase(store.getName()) + "Repository.java",
//                template);
//    }
//
//    public Output createPostmanCollection(Project.DataAccess.Solution.JavaSpringBoot module, List<MysqlDataSource.Table> tables) {
//        PostmanCollection collection = new PostmanCollection();
//
//        PostmanCollection.Info info = collection.getInfo();
//        info.setPostmanId("");
//        info.setName(module.getArtifactId());
//
//        List<PostmanCollection.Folder> folders = collection.getFolders();
//        for (MysqlDataSource.Table table: tables) {
//            PostmanCollection.Folder folder = new PostmanCollection.Folder();
//            folder.setName(table.getName());
//            folders.add(folder);
//
//            String[] verbs = new String[] { "Query", "Create", "Update", "Delete" };
//            String[] methods = new String[] { "GET", "POST", "PUT", "DELETE" };
//
//            String port = module.getServerPort().toString();
//            if (StringUtils.isBlank(port)) {
//                port = "8080";
//            }
//
//            for (int i = 0; i < methods.length; i ++) {
//                PostmanCollection.Folder.Api api = new PostmanCollection.Folder.Api();
//                folder.getApis().add(api);
//                api.setName( verbs[i] + " " + table.getName());
//                api.getRequest().setMethod(methods[i]);
//
//                PostmanCollection.Folder.Api.Url url = api.getRequest().getUrl();
//                url.setRaw("http://localhost:" + port + "/api/" + module.getArtifactId() + "/" + table.getName());
//                url.setProtocol("http");
//                url.setPort(port);
//                url.setHost(Arrays.asList("localhost"));
//                url.setPath(Arrays.asList("api", module.getArtifactId(), table.getName()));
//            }
//        }
//        return new Output(TemplateType.RAW_FILE, module.getArtifactId() + ".postman_collection.json", collection.toJson());
//    }
//}
