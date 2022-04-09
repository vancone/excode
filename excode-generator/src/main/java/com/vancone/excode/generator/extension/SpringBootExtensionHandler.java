package com.vancone.excode.generator.extension;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.vancone.excode.generator.entity.Output;
import com.vancone.excode.generator.constant.ExtensionType;
import com.vancone.excode.generator.enums.DataCarrier;
import com.vancone.excode.generator.enums.TemplateType;
import com.vancone.excode.generator.entity.DataStore;
import com.vancone.excode.generator.entity.Project;
import com.vancone.excode.generator.entity.Template;
import com.vancone.excode.generator.service.TemplateService;
import com.vancone.excode.generator.util.CompilationUnitUtil;
import com.vancone.excode.generator.util.OutputUtil;
import com.vancone.excode.generator.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @author Tenton Lien
 * @since 2021/07/24
 */
@Slf4j
@Service
public class SpringBootExtensionHandler {

    @Autowired
    private TemplateService templateService;

//    private static Module getModule(ProjectWriter writer) {
//        Project project = writer.getProject();
//        for (Module module: project.getModules()) {
//            if (module.getType().equals(ModuleType.SPRING_BOOT)) {
//                return module;
//            }
//        }
//        return null;
//    }

    public Project.DataAccess.Solution.JavaSpringBoot.Extension getExtension(Project.DataAccess.Solution.JavaSpringBoot module, String id) {
        for (Project.DataAccess.Solution.JavaSpringBoot.Extension extension: module.getExtensions()) {
            if (extension.getId().equals(id)) {
                return extension;
            }
        }
        return null;
    }

    /**
     * Extensioin Cross-Origin
     * @param module
     */
    public Output crossOrigin(Project.DataAccess.Solution.JavaSpringBoot module) {
        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_CONFIG_CROSS_ORIGIN);
        templateService.preProcess(module, template);

        CompilationUnit unit = template.parseJavaSource();
        MethodDeclaration addCorsMappingsMethod = CompilationUnitUtil.getMethodByName(unit, "addCorsMappings");
        if (addCorsMappingsMethod == null) {
            log.error("Cross Origin addCorsMappingsMethod is null");
            return null;
        }
        BlockStmt blockStmt = addCorsMappingsMethod.getBody().get();
        Statement statement = blockStmt.getStatements().get(0);
        MethodCallExpr addMappingExpr = statement.asExpressionStmt().getExpression().asMethodCallExpr();

        MethodCallExpr rootExpr = addMappingExpr;
        Project.DataAccess.Solution.JavaSpringBoot.Extension extension = getExtension(module, ExtensionType.SPRING_BOOT_CROSS_ORIGIN);

        // Add allowed origins
        String[] allowedOrigins = extension.getProperty("allowedOrigins").split(",");
        if (allowedOrigins.length > 0) {
            MethodCallExpr allowedOriginsExpr = new MethodCallExpr();
            NodeList allowedOriginsNodeList = new NodeList();
            for (String allowedOrigin: allowedOrigins) {
                allowedOriginsNodeList.add(new StringLiteralExpr(allowedOrigin));
            }
            allowedOriginsExpr.setName("allowedOrigins");
            allowedOriginsExpr.setScope(rootExpr);
            allowedOriginsExpr.setArguments(allowedOriginsNodeList);

            rootExpr = allowedOriginsExpr;
        }

        // Add allowed methods
        String[] allowedMethods = extension.getProperty("allowedMethods").split(",");
        if (allowedMethods.length > 0) {
            MethodCallExpr allowedMethodsExpr = new MethodCallExpr();
            NodeList allowedMethodsNodeList = new NodeList();
            for (String allowedMethod: allowedMethods) {
                allowedMethodsNodeList.add(new StringLiteralExpr(allowedMethod));
            }
            allowedMethodsExpr.setName("allowedMethods");
            allowedMethodsExpr.setScope(rootExpr);
            allowedMethodsExpr.setArguments(allowedMethodsNodeList);

            rootExpr = allowedMethodsExpr;
        }

        // Add allowed headers
        String[] allowedHeaders = extension.getProperty("allowedHeaders").split(",");
        if (allowedHeaders.length > 0) {
            MethodCallExpr allowedHeadersExpr = new MethodCallExpr();
            NodeList allowedHeadersNodeList = new NodeList();
            for (String allowedHeader: allowedHeaders) {
                allowedHeadersNodeList.add(new StringLiteralExpr(allowedHeader));
            }
            allowedHeadersExpr.setName("allowedHeaders");
            allowedHeadersExpr.setScope(rootExpr);
            allowedHeadersExpr.setArguments(allowedHeadersNodeList);

            rootExpr = allowedHeadersExpr;
        }
        statement.asExpressionStmt().setExpression(rootExpr);
        template.updateJavaSource(unit);

        return new Output(TemplateType.SPRING_BOOT_CONFIG_CROSS_ORIGIN,
                module.getName() + File.separator +
                "src" + File.separator + "main" + File.separator + "java" + File.separator +
                module.getGroupId().replace(".", File.separator) + File.separator +
                module.getArtifactId().replace(".", File.separator) + File.separator + "config" + File.separator + "CrossOriginConfig.java",
                template);
    }

    /**
     * Extension: Lombok
     * @param outputs
     */
    public void lombok(List<Output> outputs) {
        for (Output output: OutputUtil.getOutputsByType(outputs, TemplateType.SPRING_BOOT_ENTITY)) {
            CompilationUnit unit = StaticJavaParser.parse(output.getContent());
            ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);

            // Add annotation
            unit.addImport("lombok.Data");
            clazz.addAnnotation(new MarkerAnnotationExpr("Data"));

            // Remove getters and setters
            for (MethodDeclaration method: clazz.getMethods()) {
                String name = method.getName().toString();
                if (name.contains("get") || name.contains("set")) {
                    clazz.remove(method);
                }
            }

            output.setContent(unit.toString());
        }
    }

    /**
     * Extension: Swagger2
     * @param module
     * @param outputs
     */
    public Output swagger2(Project.DataAccess.Solution.JavaSpringBoot module, Project.DataAccess dataAccess, List<Output> outputs) {
        Template template = templateService.getTemplate(TemplateType.SPRING_BOOT_CONFIG_SWAGGER2);
        templateService.preProcess(module, template);
        template.replace("version", module.getVersion());
        template.replace("title", module.getName());
        template.replace("description", module.getDescription());

        String swaggerTags = "";
        List<DataStore> stores = dataAccess.getDataStoreByCarrier(DataCarrier.MYSQL);
        for (int i = 0; i < stores.size(); i++) {
            swaggerTags += "new Tag(\"" + StrUtil.toPascalCase(stores.get(i).getName()) + "\", " + "\"" + stores.get(i).getDescription() + "\")";
            if (i + 1 == stores.size()) {
                swaggerTags += "\n";
            } else {
                swaggerTags += ",\n";
            }
        }
        template.replace("tags", swaggerTags);

        for (DataStore store: dataAccess.getDataStoreByCarrier(DataCarrier.MYSQL)) {

            // Process entity class
            Output output = OutputUtil.getOutputByName(outputs, StrUtil.toPascalCase(store.getName()) + ".java");
            CompilationUnit unit = StaticJavaParser.parse(output.getContent());
            if (unit != null) {
                unit.addImport("io.swagger.annotations.ApiModel");
                ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);
                clazz.addAnnotation(new SingleMemberAnnotationExpr(
                        new Name("ApiModel"),
                        new StringLiteralExpr(store.getDescription())));

                // Add annotation for each field
                boolean apiModelPropertyExists = false;
                for (DataStore.Node node: store.getNodes()) {
                    if (StringUtils.isNotBlank(node.getComment())) {
                        Optional<FieldDeclaration> field = clazz.getFieldByName(StrUtil.toCamelCase(node.getName()));
                        if (field.isPresent()) {
                            field.get().addAnnotation(new SingleMemberAnnotationExpr(
                                    new Name("ApiModelProperty"),
                                    new StringLiteralExpr(node.getComment())));
                            apiModelPropertyExists = true;
                        }
                    }
                }
                if (apiModelPropertyExists) {
                    unit.addImport("io.swagger.annotations.ApiModelProperty");
                }
            } else {
                log.error("Swagger2 extension fetch entity class null");
            }
            output.setContent(unit.toString());

            // Process controller class
            output = OutputUtil.getOutputByName(outputs, StrUtil.toPascalCase(store.getName()) + "Controller.java");
            unit = StaticJavaParser.parse(output.getTemplate().getContent());
            if (unit != null) {
                unit.addImport("io.swagger.annotations.Api");
                ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);
                clazz.addAnnotation(new NormalAnnotationExpr(
                        new Name("Api"),
                        new NodeList<>(
                                new MemberValuePair(
                                        new SimpleName("tags"),
                                        new StringLiteralExpr(StrUtil.toPascalCase(store.getName()))
                                )
                        )
                ));
                output.getTemplate().updateJavaSource(unit);
            }
        }

        return new Output(TemplateType.SPRING_BOOT_CONFIG_SWAGGER2,
                module.getName() + File.separator +
                        "src" + File.separator + "main" + File.separator + "java" + File.separator +
                        module.getGroupId().replace(".", File.separator) + File.separator +
                        module.getArtifactId().replace(".", File.separator) + File.separator + "config" + File.separator + "Swagger2Config.java",
                template);
    }
}
