package controller.generators.backend;

import controller.FileManager;
import controller.Logger;
import controller.ProjectBuilder;
import model.project.Project;
import model.project.Table;

public class SqlQueryGenerator {
    public SqlQueryGenerator(Project project, String path) {
        String code = "-- " + ProjectBuilder.getDescription() + "\n\n";
        for (Table table: project.getTables()) {
            code += "CREATE TABLE " + table.getName().getData() + "\n(\n";
            for (int i = 0; i < table.getColumns().size(); i ++) {
                code += table.getColumns().get(i).getName().getData() + " " + table.getColumns().get(i).getType();
                if (table.getColumns().get(i).isPrimaryKey()) {
                    code += " NOT NULL AUTO_INCREMENT";
                }
                code += ",\n";
                if (i + 1 == table.getColumns().size()) {
                    code += "PRIMARY KEY(" + table.getPrimaryKey().getData() + ")\n";
                }
            }
            code += ");\n\n";
        }

        String sqlQueryPath = path + "/" + project.getArtifactId().getData() + ".sql";
        FileManager.write(sqlQueryPath, code);
        Logger.info("Output SQL query file \"" + sqlQueryPath + "\"");
    }
}
