package com.mekcone.incrud.service.generators.frontend.admin;

import com.mekcone.incrud.util.FileUtil;
import com.mekcone.incrud.util.TemplateUtil;
import com.mekcone.incrud.model.project.components.Column;
import com.mekcone.incrud.model.project.ProjectModel;
import com.mekcone.incrud.model.project.components.Table;

public class DataTablesVueGenerator {
    public DataTablesVueGenerator(ProjectModel projectModel, String path) {
        for (Table table: projectModel.getTables()) {
            String code = "";
            for (Column column: table.getColumns()) {
                if (column.getName().equals(table.getPrimaryKey())) {
                    continue;
                }
                code += "      <el-table-column prop=\"" +
                        column.getCamelName() + "\" label=\"" +
                        column.getCapitalizedCamelName() +
                        "\" width=\"250\"></el-table-column>\n";
            }
            TemplateUtil templateUtil = new TemplateUtil(System.getProperty("user.dir") +
                    "/templates/vue/src/components/DataTables.vue");
            templateUtil.insert("columns", code);
            templateUtil.insert("url", table.getCamelName());
            FileUtil.write(path + "/" + table.getCapitalizedCamelName() + ".vue", templateUtil.toString());
        }


    }
}
