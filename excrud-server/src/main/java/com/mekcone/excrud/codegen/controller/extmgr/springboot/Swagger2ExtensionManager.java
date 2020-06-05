package com.mekcone.excrud.codegen.controller.extmgr.springboot;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.*;
import com.mekcone.excrud.codegen.constant.ModuleExtensionType;
import com.mekcone.excrud.codegen.constant.UrlPath;
import com.mekcone.excrud.codegen.controller.generator.SpringBootGenerator;
import com.mekcone.excrud.codegen.controller.parser.template.impl.JavaTemplate;
import com.mekcone.excrud.codegen.model.module.impl.apidocument.ApiDocumentModule;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.RelationalDatabaseModule;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Table;
import com.mekcone.excrud.codegen.model.module.impl.springboot.SpringBootModule;
import com.mekcone.excrud.codegen.model.module.impl.springboot.component.SpringBootComponent;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.StrUtil;

import java.util.List;

public class Swagger2ExtensionManager {

    private Project project;
    private SpringBootGenerator callBackObject;

    public Swagger2ExtensionManager(SpringBootGenerator callBackObject, Project project) {
        this.project = project;
        this.callBackObject = callBackObject;
        SpringBootModule springBootModule = project.getModuleSet().getSpringBootModule();
        springBootModule.getProjectObjectModel().addDependencies(ModuleExtensionType.SWAGGER2);
        addConfig();
        addAnnotation();
    }

    private void addConfig() {
        ApiDocumentModule apiDocumentModule = project.getModuleSet().getApiDocumentModule();
        RelationalDatabaseModule relationalDatabaseModule = project.getModuleSet().getRelationalDatabaseModule();

        JavaTemplate javaTemplate = new JavaTemplate(UrlPath.SPRING_BOOT_TEMPLATE_PATH + "config/Swagger2Config.java");
        javaTemplate.preprocessForSpringBootProject(project, null);

        String title = apiDocumentModule.getTitle();
        if (title != null) {
            javaTemplate.insert("title", title.replace("{br}", ""));
        } else if (project.getName() != null) {
            javaTemplate.insert("title", project.getName());
        } else {
            javaTemplate.insert("title", StrUtil.capitalize(project.getArtifactId()));
        }

        String description = apiDocumentModule.getDescription();
        if (description != null) {
            javaTemplate.insert("description", description);
        } else {
            javaTemplate.insert("description", "API Documents of " + project.getName());
        }

        javaTemplate.insert("version", project.getVersion());

        String swaggerTags = "";
        List<Table> tables = relationalDatabaseModule.getDatabases().get(0).getTables();
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
