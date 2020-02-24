package com.mekcone.autocrud.core.controller.generators.frontend.admin;

import com.mekcone.autocrud.core.controller.FileManager;
import com.mekcone.autocrud.core.controller.TemplateProccesor;
import com.mekcone.autocrud.core.model.project.Project;

public class AppVueGenrator {
    public AppVueGenrator(Project project, String path) {
        TemplateProccesor templateProccesor = new TemplateProccesor(System.getProperty("user.dir") + "/templates/vue/src/App.vue");
        templateProccesor.insert("appName", project.getArtifactId().capitalizedCamelStyle() + " Admin");
        FileManager.write(path, templateProccesor.toString());
    }
}
