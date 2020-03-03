package com.mekcone.incrud.service.generators.frontend.admin;

import com.mekcone.incrud.util.FileUtil;
import com.mekcone.incrud.util.TemplateUtil;
import com.mekcone.incrud.model.project.ProjectModel;
import com.mekcone.incrud.model.project.components.Table;

public class PanelVueGenerator {
    public PanelVueGenerator(ProjectModel projectModel, String path) {
        String code = "";
        for (Table table: projectModel.getTables()) {
            code += "      <el-menu-item index=\"/" + table.getCamelName() + "\">\n";
            code += "        <span>" + table.getCapitalizedCamelName() + "</span>\n";
            code += "      </el-menu-item>\n\n";
        }

        TemplateUtil templateUtil = new TemplateUtil(System.getProperty("user.dir") + "/templates/vue/src/components/Panel.vue");
        templateUtil.insert("menuItems", code);
        FileUtil.write(path, templateUtil.toString());
    }
}
