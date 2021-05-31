package com.vancone.excode.codegen.controller.extmgr.springboot;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.vancone.excode.codegen.controller.generator.impl.SpringBootGenerator;
import com.vancone.excode.codegen.controller.parser.template.impl.JavaTemplate;
import com.vancone.excode.codegen.model.project.Project;
import com.vancone.excode.codegen.model.module.impl.SpringBootModule;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/*
 * Author: Tenton Lien
 */

@Slf4j
public class CrossOriginExtensionManager {

    private Project project;
    private SpringBootGenerator callBackObject;

    public CrossOriginExtensionManager(SpringBootGenerator callBackObject, Project project) {
        this.callBackObject = callBackObject;
        this.project = project;
        addConfig();
    }

    public void addConfig() {
        JavaTemplate javaTemplate = new JavaTemplate(callBackObject.getTemplatePath() + "config/CrossOriginConfig.java");
        if (javaTemplate != null) {
            javaTemplate.preprocessForSpringBootProject(project, null);
            MethodDeclaration addCorsMappingsMethod = javaTemplate.getMethodByName("CrossOriginConfig", "addCorsMappings");
            BlockStmt blockStmt = addCorsMappingsMethod.getBody().get();
            Statement statement = blockStmt.getStatements().get(0);
            MethodCallExpr addMappingExpr = statement.asExpressionStmt().getExpression().asMethodCallExpr();

            MethodCallExpr rootExpr = addMappingExpr;
            SpringBootModule.SpringBootExtensions springBootExtensions = project.getModuleSet().getSpringBootModule().getExtensions();

            // Add allowed origins
            List<String> allowedOrigins = springBootExtensions.getCrossOrigin().getAllowedOrigins();
            if (!allowedOrigins.isEmpty()) {
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
            List<String> allowedMethods = springBootExtensions.getCrossOrigin().getAllowedMethods();
            if (!allowedMethods.isEmpty()) {
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
            List<String> allowedHeaders = springBootExtensions.getCrossOrigin().getAllowedHeaders();
            if (!allowedHeaders.isEmpty()) {
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
            callBackObject.addOutputFile(callBackObject.getPath("configPath") + "CrossOriginConfig.java", javaTemplate.toString());
        } else {
            log.error("CrossOriginConfig template file not found");
        }
    }
}
