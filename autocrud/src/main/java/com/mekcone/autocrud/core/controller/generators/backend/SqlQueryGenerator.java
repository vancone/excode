package com.mekcone.autocrud.core.controller.generators.backend;

import com.mekcone.autocrud.core.controller.Logger;
import com.mekcone.autocrud.core.controller.ProjectBuilder;
import com.mekcone.autocrud.core.model.project.Project;
import com.mekcone.autocrud.core.model.project.Table;
import com.mekcone.autocrud.core.controller.FileManager;

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
