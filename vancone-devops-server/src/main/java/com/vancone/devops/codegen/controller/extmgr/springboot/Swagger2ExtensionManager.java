package com.vancone.devops.codegen.controller.extmgr.springboot;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vancone.devops.codegen.controller.generator.impl.SpringBootGenerator;
import com.vancone.devops.codegen.controller.parser.template.impl.JavaTemplate;
import com.vancone.devops.codegen.model.database.Table;
import com.vancone.devops.codegen.model.project.Project;
import com.vancone.devops.constant.ModuleConstant;
import com.vancone.devops.codegen.model.module.impl.DatasourceModule;
import com.vancone.devops.codegen.model.module.impl.SpringBootModule;
import com.vancone.devops.codegen.model.file.springboot.SpringBootComponent;
import com.vancone.devops.codegen.util.LangUtil;

import java.util.List;

/*
 * Author: Tenton Lien
 */

public class Swagger2ExtensionManager {

    private Project project;
    private SpringBootGenerator callBackObject;

    public Swagger2ExtensionManager(SpringBootGenerator callBackObject, Project project) {
        this.project = project;
        this.callBackObject = callBackObject;
        SpringBootModule springBootModule = project.getModuleSet().getSpringBootModule();
        springBootModule.getMavenProjectObjectModel().addDependencies(ModuleConstant.SPRING_BOOT_EXTENSION_SWAGGER2);
        addConfig();
        addAnnotation();
    }

    private void addConfig() {
        DatasourceModule datasourceModule = project.getModuleSet().getDatasourceModule();

        JavaTemplate javaTemplate = new JavaTemplate(callBackObject.getTemplatePath() + "config/Swagger2Config.java");
        javaTemplate.preprocessForSpringBootProject(project, null);

        String title = project.getName().getDefaultValue() + LangUtil.separator(project.getDefaultLanguage()) + LangUtil.get(project.getDefaultLanguage(), "api_document");
        javaTemplate.insert("title", title);

        String description = project.getDescription().getDefaultValue();
        if (description != null) {
            javaTemplate.insert("description", description);
        } else {
            javaTemplate.insert("description", "API Documents of " + project.getName());
        }

        javaTemplate.insert("version", project.getVersion());

        String swaggerTags = "";
        List<Table> tables = datasourceModule.getRelationalDatabase().getDatabases().get(0).getTables();
        for (int i = 0; i < tables.size(); i++) {
            swaggerTags += "new Tag(\"" + tables.get(i).getUpperCamelCaseName() + "\", " + "\"" + tables.get(i).getDescription() + "\")";
            if (i + 1 == tables.size()) {
                swaggerTags += "\n";
            } else {
                swaggerTags += ",\n";
            }
        }
        javaTemplate.insert("tags", swaggerTags);
        callBackObject.addOutputFile(callBackObject.getPath("configPath") + "Swagger2Config.java", javaTemplate.toString());
    }

    private void addAnnotation() {
        SpringBootModule springBootModule = project.getModuleSet().getSpringBootModule();

        // Add Api annotation to each controller
        for (SpringBootComponent springBootComponent: springBootModule.getControllers()) {
            springBootComponent.addImport("io.swagger.annotations.Api");
            NormalAnnotationExpr apiAnnotationExpr = new NormalAnnotationExpr();
            apiAnnotationExpr.setName("Api");

            // Add Swagger2 tag name into annotation
            NodeList nodeList = new NodeList();
            nodeList.add(new StringLiteralExpr(springBootComponent.getName().replace("Controller", "")));
            ArrayInitializerExpr arrayInitializerExpr = new ArrayInitializerExpr();
            arrayInitializerExpr.setValues(nodeList);

            apiAnnotationExpr.addPair("tags", arrayInitializerExpr);
            springBootComponent.addClassAnnotation(apiAnnotationExpr);
        }
    }

}
