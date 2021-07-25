package com.vancone.excode.core.extension;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.*;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.vancone.excode.core.ProjectWriter;
import com.vancone.excode.core.TemplateFactory;
import com.vancone.excode.core.constant.ExtensionType;
import com.vancone.excode.core.constant.ModuleType;
import com.vancone.excode.core.enums.TemplateType;
import com.vancone.excode.core.model.Module;
import com.vancone.excode.core.model.Project;
import com.vancone.excode.core.model.Template;
import com.vancone.excode.core.model.datasource.MysqlDataSource;
import com.vancone.excode.core.util.CompilationUnitUtil;
import com.vancone.excode.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Optional;

/**
 * @author Tenton Lien
 * @date 7/24/2021
 */
@Slf4j
public class SpringBootExtensionHandler {

    private static Module getModule(ProjectWriter writer) {
        Project project = writer.getProject();
        for (Module module: project.getModules()) {
            if (module.getType().equals(ModuleType.SPRING_BOOT)) {
                return module;
            }
        }
        return null;
    }

    private static Module getExtension(ProjectWriter writer, String name) {
        Module module = getModule(writer);
        for (Module extension: module.getExtensions()) {
            if (extension.getType().equals(name)) {
                return extension;
            }
        }
        return null;
    }

    /**
     * Extensioin Cross-Origin
     * @param writer
     */
    public static void crossOrigin(Module module, ProjectWriter writer) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_CONFIG_CROSS_ORIGIN);
        TemplateFactory.preProcess(writer.getProject(), template);

        CompilationUnit unit = template.parseJavaSource();
        MethodDeclaration addCorsMappingsMethod = CompilationUnitUtil.getMethodByName(unit, "addCorsMappings");
        if (addCorsMappingsMethod == null) {
            log.error("Cross Origin addCorsMappingsMethod is null");
            return;
        }
        BlockStmt blockStmt = addCorsMappingsMethod.getBody().get();
        Statement statement = blockStmt.getStatements().get(0);
        MethodCallExpr addMappingExpr = statement.asExpressionStmt().getExpression().asMethodCallExpr();

        MethodCallExpr rootExpr = addMappingExpr;
        Module extension = getExtension(writer, ExtensionType.SPRING_BOOT_CROSS_ORIGIN);

        // Add allowed origins
        String[] allowedOrigins = extension.getProperty("allowed-origins").split(",");
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
        String[] allowedMethods = extension.getProperty("allowed-methods").split(",");
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
        String[] allowedHeaders = extension.getProperty("allowed-headers").split(",");
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

        writer.addOutput(TemplateType.SPRING_BOOT_CONFIG_CROSS_ORIGIN,
                module.getName() + File.separator +
                "src" + File.separator + "main" + File.separator + "java" + File.separator +
                writer.getProject().getGroupId().replace(".", File.separator) + File.separator +
                writer.getProject().getArtifactId().replace(".", File.separator) + File.separator + "config" + File.separator + "CrossOriginConfig.java",
                template);
    }

    /**
     * Extension: Lombok
     * @param writer
     */
    public static void lombok(Module module, ProjectWriter writer) {
        for (ProjectWriter.Output output: writer.getOutputsByType(TemplateType.SPRING_BOOT_ENTITY)) {
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
     * @param writer
     */
    public static void swagger2(Module module, ProjectWriter writer) {
        Template template = TemplateFactory.getTemplate(TemplateType.SPRING_BOOT_CONFIG_SWAGGER2);
        TemplateFactory.preProcess(writer.getProject(), template);
        template.replace("version", writer.getProject().getVersion());
        template.replace("title", writer.getProject().getDefaultName());
        template.replace("description", writer.getProject().getDefaultDescription());

        String swaggerTags = "";
        List<MysqlDataSource.Table> tables = writer.getProject().getDatasource().getMysql().getTables();
        for (int i = 0; i < tables.size(); i++) {
            swaggerTags += "new Tag(\"" + tables.get(i).getUpperCamelCaseName() + "\", " + "\"" + tables.get(i).getDescription() + "\")";
            if (i + 1 == tables.size()) {
                swaggerTags += "\n";
            } else {
                swaggerTags += ",\n";
            }
        }
        template.replace("tags", swaggerTags);

        writer.addOutput(TemplateType.SPRING_BOOT_CONFIG_SWAGGER2,
                module.getName() + File.separator +
                "src" + File.separator + "main" + File.separator + "java" + File.separator +
                writer.getProject().getGroupId().replace(".", File.separator) + File.separator +
                writer.getProject().getArtifactId().replace(".", File.separator) + File.separator + "config" + File.separator + "Swagger2Config.java",
                template);

        for (MysqlDataSource.Table table: writer.getProject().getDatasource().getMysql().getTables()) {

            // Process entity class
            ProjectWriter.Output output = writer.getOutputByName(StrUtil.upperCamelCase(table.getName()) + ".java");
            CompilationUnit unit = StaticJavaParser.parse(output.getContent());
            if (unit != null) {
                unit.addImport("io.swagger.annotations.ApiModel");
                ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);
                clazz.addAnnotation(new SingleMemberAnnotationExpr(
                        new Name("ApiModel"),
                        new StringLiteralExpr(table.getDescription())));

                // Add annotation for each field
                boolean apiModelPropertyExists = false;
                for (MysqlDataSource.Table.Column column: table.getColumns()) {
                    if (StringUtils.isNotBlank(column.getComment())) {
                        Optional<FieldDeclaration> field = clazz.getFieldByName(column.getCamelCaseName(table.getName()));
                        if (field.isPresent()) {
                            field.get().addAnnotation(new SingleMemberAnnotationExpr(
                                    new Name("ApiModelProperty"),
                                    new StringLiteralExpr(column.getComment())));
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
            output = writer.getOutputByName(StrUtil.upperCamelCase(table.getName()) + "Controller.java");
            unit = StaticJavaParser.parse(output.getTemplate().getContent());
            if (unit != null) {
                unit.addImport("io.swagger.annotations.Api");
                ClassOrInterfaceDeclaration clazz = CompilationUnitUtil.getMainClassOrInterface(unit);
                clazz.addAnnotation(new NormalAnnotationExpr(
                        new Name("Api"),
                        new NodeList<>(
                                new MemberValuePair(
                                        new SimpleName("tags"),
                                        new StringLiteralExpr(table.getUpperCamelCaseName())
                                )
                        )
                ));
            }
            output.getTemplate().updateJavaSource(unit);
        }
    }
}
