package com.mekcone.incrud.model.file.sql;

import com.mekcone.incrud.model.file.FileModel;
import com.mekcone.incrud.model.project.components.Table;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SqlFileModel extends FileModel {

    private List<Table> tables;

    public void setTables(List<Table> tables) {
        this.tables =tables;
    }

    @Override
    public String toString() {
        String code = "-- " + getDescription() + "\n\n";
        for (Table table: this.tables) {
            code += "CREATE TABLE " + table.getName() + "\n(\n";
            for (int i = 0; i < table.getColumns().size(); i ++) {
                code += table.getColumns().get(i).getName() + " " + table.getColumns().get(i).getType();
                if (table.getColumns().get(i).isPrimaryKey()) {
                    code += " NOT NULL AUTO_INCREMENT";
                }
                code += ",\n";
                if (i + 1 == table.getColumns().size()) {
                    code += "PRIMARY KEY(" + table.getPrimaryKey() + ")\n";
                }
            }
            code += ");\n\n";
        }
        return code;
    }
}
