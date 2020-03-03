package com.mekcone.incrud.service.generators.frontend.admin;

import com.mekcone.incrud.util.FileUtil;
import com.mekcone.incrud.util.TemplateUtil;
import com.mekcone.incrud.model.project.ProjectModel;
import com.mekcone.incrud.util.StringUtil;

public class AppVueGenrator {
    public AppVueGenrator(ProjectModel projectModel, String path) {
        TemplateUtil templateUtil = new TemplateUtil(System.getProperty("user.dir") + "/templates/vue/src/App.vue");
        templateUtil.insert("appName", StringUtil.capitalizedCamel(projectModel.getArtifactId()) + " Admin");
        FileUtil.write(path, templateUtil.toString());
    }
}
