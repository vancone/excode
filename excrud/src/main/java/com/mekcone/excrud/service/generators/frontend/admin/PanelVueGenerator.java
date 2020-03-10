/*
package com.mekcone.excrud.service.generators.frontend.admin;

import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.model.Template;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.components.Table;

public class PanelVueGenerator {
    public PanelVueGenerator(Project project, String path) {
        String code = "";
        for (Table table: project.getDatabases().get(0).getTables()) {
            code += "      <el-menu-item index=\"/" + table.getCamelName() + "\">\n";
            code += "        <span>" + table.getCapitalizedCamelName() + "</span>\n";
            code += "      </el-menu-item>\n\n";
        }

        Template template = new Template(System.getProperty("user.dir") + "/templates/vue/src/components/Panel.vue");
        template.insert("menuItems", code);
        FileUtil.write(path, template.toString());
    }
}
*/
