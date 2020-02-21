package controller.generators.frontend.admin;

import controller.FileManager;
import controller.TemplateProccesor;
import model.project.Column;
import model.project.Project;
import model.project.Table;

public class DataTablesVueGenerator {
    public DataTablesVueGenerator(Project project, String path) {
        for (Table table: project.getTables()) {
            String code = "";
            for (Column column: table.getColumns()) {
                if (column.getName().getData().equals(table.getPrimaryKey().getData())) {
                    continue;
                }
                code += "      <el-table-column prop=\"" +
                        column.getName().camelStyle() + "\" label=\"" +
                        column.getName().capitalizedCamelStyle() +
                        "\" width=\"250\"></el-table-column>\n";
            }
            TemplateProccesor templateProccesor = new TemplateProccesor(System.getProperty("user.dir") +
                    "/templates/vue/src/components/DataTables.vue");
            templateProccesor.insert("columns", code);
            templateProccesor.insert("url", table.getName().camelStyle());
            FileManager.write(path + "/" + table.getName().capitalizedCamelStyle() + ".vue", templateProccesor.toString());
        }


    }
}
