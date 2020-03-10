/*
package com.mekcone.excrud.service.generators.frontend.admin;

import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.model.Template;
import com.mekcone.excrud.model.project.components.Column;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.components.Table;

public class DataTablesVueGenerator {
    public DataTablesVueGenerator(Project project, String path) {
        for (Table table: project.getDatabases().get(0).getTables()) {
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
            Template template = new Template(System.getProperty("user.dir") +
                    "/templates/vue/src/components/DataTables.vue");
            template.insert("columns", code);
            template.insert("url", table.getCamelName());
            FileUtil.write(path + "/" + table.getCapitalizedCamelName() + ".vue", template.toString());
        }


    }
}
*/
