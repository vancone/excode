package controller.generators;

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
                code += Module.getPrefix(module.getId()) + property.getKey() + " = " + property.getValue() + "\n";
            }
            code += "\n";
        }

        FileManager.write(path + "/application.properties", code);
        Logger.info("Output application class file \"" + path + "/application.properties\"");
    }
}
