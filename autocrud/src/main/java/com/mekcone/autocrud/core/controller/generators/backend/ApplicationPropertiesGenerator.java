package com.mekcone.autocrud.core.controller.generators.backend;

import com.mekcone.autocrud.core.controller.Logger;
import com.mekcone.autocrud.core.controller.ProjectBuilder;
import com.mekcone.autocrud.core.model.project.Module;
import com.mekcone.autocrud.core.model.project.Project;
import com.mekcone.autocrud.core.model.project.Property;
import com.mekcone.autocrud.core.controller.FileManager;

public class ApplicationPropertiesGenerator {
    public ApplicationPropertiesGenerator(Project project, String path) {
        String code = "";
        code += "# " + ProjectBuilder.getDescription() + "\n\n";

        for (Module module: project.getModules()) {
            code += "# " + module.getId() + "\n";
            for (Property property: module.getConfig()) {
                if (module.getId().equals("mysql") && property.getKey().equals("url")) {
                    code += "spring.datasource.driver-class-name = com.mysql.cj.jdbc.Driver\n";
                    code += Module.getPrefix(module.getId()) + property.getKey() + " = jdbc:mysql://" + property.getValue() + "\n";
                    continue;
                }
                code += Module.getPrefix(module.getId()) + property.getKey() + " = " + property.getValue() + "\n";
            }
            code += "\n";
        }

        FileManager.write(path + "/application.properties", code);
        Logger.info("Output application class file \"" + path + "/application.properties\"");
    }
}
