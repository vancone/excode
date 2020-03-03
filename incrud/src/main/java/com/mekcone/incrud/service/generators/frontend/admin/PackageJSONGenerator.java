package com.mekcone.incrud.service.generators.frontend.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mekcone.incrud.model.file.json.PackageJSON;
import com.mekcone.incrud.model.project.ProjectModel;
import com.mekcone.incrud.util.FileUtil;
import com.mekcone.incrud.util.LogUtil;

public class PackageJSONGenerator {
    public PackageJSONGenerator(ProjectModel projectModel, String path) {

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String packageJSONTemplate = FileUtil.read(System.getProperty("user.dir") + "/templates/vue/package.json");
        PackageJSON packageJSON = gson.fromJson(packageJSONTemplate, PackageJSON.class);
        packageJSON.setName(projectModel.getArtifactId());
        packageJSON.setVersion(projectModel.getVersion());
        packageJSON.setDescription(projectModel.getDescription());
        packageJSON.setAuthor(projectModel.getGroupId());

        FileUtil.write(path + "/package.json", gson.toJson(packageJSON));
        LogUtil.info("Output webpack project file \"" + path + "/package.json" + "\"");
    }
}
