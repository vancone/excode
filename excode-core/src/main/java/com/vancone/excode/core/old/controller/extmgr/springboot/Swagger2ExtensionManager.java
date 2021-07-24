package com.vancone.excode.core.old.controller.extmgr.springboot;

import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.vancone.excode.core.old.constant.ModuleConstant;
import com.vancone.excode.core.old.controller.generator.impl.SpringBootGenerator;
import com.vancone.excode.core.old.controller.parser.template.impl.JavaTemplate;
import com.vancone.excode.core.old.model.database.Table;
import com.vancone.excode.core.old.model.file.springboot.SpringBootComponent;
import com.vancone.excode.core.old.model.module.impl.DatasourceModule;
import com.vancone.excode.core.old.model.module.impl.SpringBootModule;
import com.vancone.excode.core.old.model.project.ProjectOld;
import com.vancone.excode.core.util.LangUtil;

import java.util.List;

/**
 * @author Tenton Lien
 */
public class Swagger2ExtensionManager {

    private ProjectOld projectOld;
    private SpringBootGenerator callBackObject;

    public Swagger2ExtensionManager(SpringBootGenerator callBackObject, ProjectOld projectOld) {
        this.projectOld = projectOld;
        this.callBackObject = callBackObject;
        SpringBootModule springBootModule = projectOld.getModuleSet().getSpringBootModule();
        springBootModule.getMavenPom().addDependenciesOld(ModuleConstant.SPRING_BOOT_EXTENSION_SWAGGER2);
        addConfig();
        addAnnotation();
    }

    private void addConfig() {
        DatasourceModule datasourceModule = projectOld.getModuleSet().getDatasourceModule();

        JavaTemplate javaTemplate = new JavaTemplate(callBackObject.getTemplatePath() + "config/Swagger2Config.java");
        javaTemplate.preprocessForSpringBootProject(projectOld, null);

        String title = projectOld.getName().getDefaultValue() + LangUtil.separator(projectOld.getDefaultLanguage()) + LangUtil.get(projectOld.getDefaultLanguage(), "api_document");
        javaTemplate.insert("title", title);

        String description = projectOld.getDescription().getDefaultValue();
        if (description != null) {
            javaTemplate.insert("description", description);
        } else {
            javaTemplate.insert("description", "API Documents of " + projectOld.getName());
        }

        javaTemplate.insert("version", projectOld.getVersion());

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
        SpringBootModule springBootModule = projectOld.getModuleSet().getSpringBootModule();

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
