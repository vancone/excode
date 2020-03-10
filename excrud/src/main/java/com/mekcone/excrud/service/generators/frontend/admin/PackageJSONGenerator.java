package com.mekcone.excrud.service.generators.frontend.admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mekcone.excrud.model.file.json.PackageJSON;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;

public class PackageJSONGenerator {
    public PackageJSONGenerator(Project project, String path) {

        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String packageJSONTemplate = FileUtil.read(System.getProperty("user.dir") + "/templates/vue/package.json");
        PackageJSON packageJSON = gson.fromJson(packageJSONTemplate, PackageJSON.class);
        packageJSON.setName(project.getArtifactId());
        packageJSON.setVersion(project.getVersion());
        packageJSON.setDescription(project.getDescription());
        packageJSON.setAuthor(project.getGroupId());

        FileUtil.write(path + "/package.json", gson.toJson(packageJSON));
        LogUtil.info("Output webpack project file \"" + path + "/package.json" + "\"");
    }
}
