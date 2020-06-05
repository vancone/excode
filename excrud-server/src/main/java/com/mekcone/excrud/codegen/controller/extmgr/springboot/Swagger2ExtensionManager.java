package com.mekcone.excrud.codegen.controller.extmgr.springboot;

import com.mekcone.excrud.codegen.constant.ModuleExtension;
import com.mekcone.excrud.codegen.controller.extmgr.SpringBootExtensionManager;
import com.mekcone.excrud.codegen.controller.parser.template.impl.UniversalTemplate;
import com.mekcone.excrud.codegen.model.module.impl.apidocument.ApiDocumentModule;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.RelationalDatabaseModule;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Table;
import com.mekcone.excrud.codegen.model.module.impl.springboot.SpringBootModule;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.util.StrUtil;

import java.util.List;

public class Swagger2ExtensionManager extends SpringBootExtensionManager {

    public Swagger2ExtensionManager(Project project) {
        this.project = project;
        SpringBootModule springBootModule = project.getModuleSet().getSpringBootModule();
        springBootModule.getProjectObjectModel().addDependencies(ModuleExtension.SWAGGER2);
        addConfig();

    }

    @Override
    public void addConfig() {
        /*ApiDocumentModule apiDocumentModule = project.getModuleSet().getApiDocumentModule();
        RelationalDatabaseModule relationalDatabaseModule = project.getModuleSet().getRelationalDatabaseModule();

        UniversalTemplate universalTemplate = new UniversalTemplate(springBootGenerator.getTemplatePath() + "config/Swagger2Config.java");
        springBootGenerator.preprocessTemplate(universalTemplate);

        String title = apiDocumentModule.getTitle();
        if (title != null) {
            universalTemplate.insert("title", title.replace("{br}", ""));
        } else if (project.getName() != null) {
            universalTemplate.insert("title", project.getName());
        } else {
            universalTemplate.insert("title", StrUtil.capitalize(project.getArtifactId()));
        }

        String description = apiDocumentModule.getDescription();
        if (description != null) {
            universalTemplate.insert("description", description);
        } else {
            universalTemplate.insert("description", "API Documents of " + project.getName());
        }

        universalTemplate.insert("version", project.getVersion());

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
        universalTemplate.insert("tags", swaggerTags);*/

        //springBootGenerator.addOutputFile("" + "Swagger2Config.java", SpringBootComponentType.CONFIG, universalTemplate.toString());
    }

}
