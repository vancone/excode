/*
package com.mekcone.excrud.service.generators.frontend.admin;

import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.model.Template;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.util.StringUtil;

public class AppVueGenrator {
    public AppVueGenrator(Project project, String path) {
        Template template = new Template(System.getProperty("user.dir") + "/templates/vue/src/App.vue");
        template.insert("appName", StringUtil.capitalizedCamel(project.getArtifactId()) + " Admin");
        FileUtil.write(path, template.toString());
    }
}
*/
