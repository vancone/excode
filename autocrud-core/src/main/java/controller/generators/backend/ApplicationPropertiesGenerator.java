package controller.generators.backend;

import controller.FileManager;
import controller.Logger;
import controller.ProjectBuilder;
import model.project.Module;
import model.project.Project;
import model.project.Property;

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
