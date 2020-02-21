package controller.generators.frontend.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import controller.FileManager;
import model.json.PackageJSON;
import model.project.Project;
import controller.Logger;

public class PackageJSONGenerator {
    public PackageJSONGenerator(Project project, String path) {

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String packageJSONTemplate = FileManager.read(System.getProperty("user.dir") + "/templates/vue/package.json");
        PackageJSON packageJSON = gson.fromJson(packageJSONTemplate, PackageJSON.class);
        packageJSON.setName(project.getArtifactId().getData());
        packageJSON.setVersion(project.getVersion());
        packageJSON.setDescription(project.getDescription());
        packageJSON.setAuthor(project.getGroupId());

        FileManager.write(path + "/package.json", gson.toJson(packageJSON));
        Logger.info("Output webpack project file \"" + path + "/package.json" + "\"");
    }
}
