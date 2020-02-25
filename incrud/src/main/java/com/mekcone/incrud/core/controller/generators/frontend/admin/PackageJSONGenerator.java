package com.mekcone.incrud.core.controller.generators.frontend.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mekcone.incrud.core.model.json.PackageJSON;
import com.mekcone.incrud.core.model.project.Project;
import com.mekcone.incrud.core.controller.FileManager;
import com.mekcone.incrud.core.controller.Logger;

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
