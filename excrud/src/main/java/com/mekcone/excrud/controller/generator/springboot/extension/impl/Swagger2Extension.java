package com.mekcone.excrud.controller.generator.springboot.extension.impl;

import com.mekcone.excrud.controller.generator.springboot.SpringBootGenerator;
import com.mekcone.excrud.controller.generator.springboot.extension.SpringBootExtension;
import com.mekcone.excrud.controller.parser.template.impl.UniversalTemplate;
import com.mekcone.excrud.model.project.export.impl.relationaldatabase.database.Table;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.util.StrUtil;

import java.util.List;

public class Swagger2Extension extends SpringBootExtension {
    private static SpringBootGenerator springBootGenerator;

    public static void execute(SpringBootGenerator localSpringBootGenerator) {
        springBootGenerator = localSpringBootGenerator;
        addConfig();
        for (var outputFile: springBootGenerator.getOutputFiles()) {
            if (outputFile.isType(SpringBootGenerator.CONTROLLER_TYPE)) {

            }
        }
    }

    public static void addConfig() {
        Project project = springBootGenerator.getProject();
        UniversalTemplate universalTemplate = new UniversalTemplate(springBootGenerator.getTemplatePath() + "config/Swagger2Config.java");
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
        List<Table> tables = project.getExports().getDatabases().get(0).getTables();
        for (var i = 0; i < tables.size(); i++) {
            swaggerTags += "new Tag(\"" + tables.get(i).getUpperCamelCaseName() + "\", " + "\"" + tables.get(i).getDescription() + "\")";
            if (i + 1 == tables.size()) {
                swaggerTags += "\n";
            } else {
                swaggerTags += ",\n";
            }
        }
        universalTemplate.insert("tags", swaggerTags);

        springBootGenerator.addOutputFile("" + "Swagger2Config.java", SpringBootGenerator.CONFIG_TYPE, universalTemplate.toString());
    }


    public void execute() {

    }
}
