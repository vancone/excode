package com.mekcone.autocrud.core.controller.generators.frontend.admin;

import com.mekcone.autocrud.core.controller.FileManager;
import com.mekcone.autocrud.core.controller.TemplateProccesor;
import com.mekcone.autocrud.core.model.project.Project;
import com.mekcone.autocrud.core.model.project.Table;

public class PanelVueGenerator {
    public PanelVueGenerator(Project project, String path) {
        String code = "";
        for (Table table: project.getTables()) {
            code += "      <el-menu-item index=\"/" + table.getName().camelStyle() + "\">\n";
            code += "        <span>" + table.getName().capitalizedCamelStyle() + "</span>\n";
            code += "      </el-menu-item>\n\n";
        }

        TemplateProccesor templateProccesor = new TemplateProccesor(System.getProperty("user.dir") + "/templates/vue/src/components/Panel.vue");
        templateProccesor.insert("menuItems", code);
        FileManager.write(path, templateProccesor.toString());
    }
}
