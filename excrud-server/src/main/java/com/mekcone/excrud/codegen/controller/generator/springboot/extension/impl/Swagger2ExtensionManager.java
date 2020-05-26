package com.mekcone.excrud.codegen.controller.generator.springboot.extension.impl;

import com.mekcone.excrud.codegen.controller.generator.springboot.extension.SpringBootExtensionManager;
import com.mekcone.excrud.codegen.model.export.impl.springboot.SpringBootGenModel;

public class Swagger2ExtensionManager extends SpringBootExtensionManager {

    public Swagger2ExtensionManager(SpringBootGenModel springBootGenModel) {
        this.springBootGenModel = springBootGenModel;
        addConfig();

    }

    @Override
    public void addConfig() {
        /*UniversalTemplate universalTemplate = new UniversalTemplate(SpringBootGenerator.getTemplatePath() + "config/Swagger2Config.java");
        springBootGenerator.preprocessTemplate(universalTemplate);

        String title = project.getExports().getApiDocumentExport().getTitle();
        if (title != null) {
            universalTemplate.insert("title", title.replace("{br}", ""));
        } else if (project.getName() != null) {
            universalTemplate.insert("title", project.getName());
        } else {
            universalTemplate.insert("title", StrUtil.capitalize(project.getArtifactId()));
        }

        String description = project.getExports().getApiDocumentExport().getDescription();
        if (description != null) {
            universalTemplate.insert("description", description);
        } else {
            universalTemplate.insert("description", "API Documents of " + project.getName());
        }

        universalTemplate.insert("version", project.getVersion());

        String swaggerTags = "";
        List<Table> tables = project.getExports().getRelationalDatabaseExport().getDatabases().get(0).getTables();
        for (var i = 0; i < tables.size(); i++) {
            swaggerTags += "new Tag(\"" + tables.get(i).getUpperCamelCaseName() + "\", " + "\"" + tables.get(i).getDescription() + "\")";
            if (i + 1 == tables.size()) {
                swaggerTags += "\n";
            } else {
                swaggerTags += ",\n";
            }
        }
        universalTemplate.insert("tags", swaggerTags);*/

        //springBootGenerator.addOutputFile("" + "Swagger2Config.java", SpringBootComponentType.CONFIG, universalTemplate.toString());
    }

}
