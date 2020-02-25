package com.mekcone.incrud.core.controller.generators.frontend.admin;

import com.mekcone.incrud.core.controller.FileManager;
import com.mekcone.incrud.core.controller.TemplateProccesor;
import com.mekcone.incrud.core.model.project.Project;

public class AppVueGenrator {
    public AppVueGenrator(Project project, String path) {
        TemplateProccesor templateProccesor = new TemplateProccesor(System.getProperty("user.dir") + "/templates/vue/src/App.vue");
        templateProccesor.insert("appName", project.getArtifactId().capitalizedCamelStyle() + " Admin");
        FileManager.write(path, templateProccesor.toString());
    }
}
