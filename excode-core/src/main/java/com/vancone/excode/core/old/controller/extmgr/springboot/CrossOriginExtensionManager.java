package com.vancone.excode.core.old.controller.extmgr.springboot;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.vancone.excode.core.old.controller.generator.impl.SpringBootGenerator;
import com.vancone.excode.core.old.controller.parser.template.impl.JavaTemplate;
import com.vancone.excode.core.old.model.module.impl.SpringBootModule;
import com.vancone.excode.core.old.model.project.ProjectOld;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Tenton Lien
 */
@Slf4j
public class CrossOriginExtensionManager {

    private ProjectOld projectOld;
    private SpringBootGenerator callBackObject;

    public CrossOriginExtensionManager(SpringBootGenerator callBackObject, ProjectOld projectOld) {
        this.callBackObject = callBackObject;
        this.projectOld = projectOld;
        addConfig();
    }

    public void addConfig() {
        JavaTemplate javaTemplate = new JavaTemplate(callBackObject.getTemplatePath() + "config/CrossOriginConfig.java");
        if (javaTemplate != null) {
            javaTemplate.preprocessForSpringBootProject(projectOld, null);
            MethodDeclaration addCorsMappingsMethod = javaTemplate.getMethodByName("CrossOriginConfig", "addCorsMappings");
            BlockStmt blockStmt = addCorsMappingsMethod.getBody().get();
            Statement statement = blockStmt.getStatements().get(0);
            MethodCallExpr addMappingExpr = statement.asExpressionStmt().getExpression().asMethodCallExpr();

            MethodCallExpr rootExpr = addMappingExpr;
            SpringBootModule.SpringBootProperties springBootProperties = projectOld.getModuleSet().getSpringBootModule().getProperties();

            // Add allowed origins
            List<String> allowedOrigins = springBootProperties.getCrossOrigin().getAllowedOrigins();
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
            List<String> allowedMethods = springBootProperties.getCrossOrigin().getAllowedMethods();
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
            List<String> allowedHeaders = springBootProperties.getCrossOrigin().getAllowedHeaders();
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
