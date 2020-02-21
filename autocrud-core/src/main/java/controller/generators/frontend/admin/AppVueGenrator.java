package controller.generators.frontend.admin;

import controller.FileManager;
import controller.TemplateProccesor;
import model.project.Project;

public class AppVueGenrator {
    public AppVueGenrator(Project project, String path) {
        TemplateProccesor templateProccesor = new TemplateProccesor(System.getProperty("user.dir") + "/templates/vue/src/App.vue");
        templateProccesor.insert("appName", project.getArtifactId().capitalizedCamelStyle() + " Admin");
        FileManager.write(path, templateProccesor.toString());
    }
}
