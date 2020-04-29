package com.mekcone.excrud.controller.generator;

import com.mekcone.excrud.Application;
import com.mekcone.excrud.model.project.export.impl.relationaldatabase.database.Table;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.project.export.impl.relationaldatabase.database.Database;
import com.mekcone.excrud.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlGenerator extends BaseGenerator {

    public SqlGenerator(Project project) {
        this.project = project;
    }

    public static String createDatabaseQuery(Database database) {
        return "CREATE DATABASE " + database.getName() + ";";
    }

    public static String createTableQuery(Table table) {
        String query = "CREATE TABLE " + table.getName() + " (\n";
        for (var i = 0; i < table.getColumns().size(); i++) {
            query += "    " + table.getColumns().get(i).getName() + " " + table.getColumns().get(i).getType();
            if (table.getColumns().get(i).isPrimaryKey()) {
                query += " NOT NULL AUTO_INCREMENT";
            }
            query += ",\n";
            if (i + 1 == table.getColumns().size()) {
                query += "    PRIMARY KEY(" + table.getPrimaryKey() + ")\n";
            }
        }
        query += ");";
        return query;
    }

    public static String insertQuery(Table table, boolean ignorePrimaryKey) {
        String query = "INSERT INTO " + table.getName() + " (";
        // If the last column is a primary key, this block of codes may not work out the correct result
        for (var i = 0; i < table.getColumns().size(); i ++) {
            if (ignorePrimaryKey && table.getColumns().get(i).isPrimaryKey()) {
                continue;
            }
            query += table.getColumns().get(i).getName();
            if (i + 1 != table.getColumns().size()) {
                query += ", ";
            } else {
                query +=") ";
            }
        }
        query += "VALUES(";
        for (var i = 0; i < table.getColumns().size(); i ++) {
            if (ignorePrimaryKey && table.getColumns().get(i).isPrimaryKey()) {
                continue;
            }
            query += "#{" + table.getColumns().get(i).getCamelCaseName(table.getName()) + "}";
            if (i + 1 != table.getColumns().size()) {
                query += ", ";
            } else {
                query += ")";
            }
        }
        return query;
    }

    public static String updateQuery(Table table) {
        String query = "UPDATE " + table.getName() + " SET ";
        for (var i = 0; i < table.getColumns().size(); i ++) {
            if (table.getColumns().get(i).isPrimaryKey()) {
                continue;
            }
            query += table.getColumns().get(i).getName() + "=#{" + table.getColumns().get(i).getCamelCaseName(table.getName()) + "}";
            if (i + 1 != table.getColumns().size()) {
                query += ", ";
            }
        }
        query += " WHERE " + table.getPrimaryKey() + "=#{" + table.getCamelCasePrimaryKey() + "}";
        return query;
    }

    @Override
    public void generate() {
        //FileUtil.checkDirectory("sql");
        String code = "";
        if (Application.getDescription() != null) {
            code += "-- " + Application.getDescription() + "\n\n";
        }
        for (var database: project.getExports().getDatabases()) {
            if (database != null && database.hasTable()) {
                code += createDatabaseQuery(database) + "\n\n";
                for (var table: database.getTables()) {
                    if (table != null && table.hasColumn()) {
                        code += createTableQuery(table) + "\n\n";
                    }
                }
            }
        }
        FileUtil.write(project.getArtifactId() + ".sql", code);
        log.info("Generate SQL queries completed");
    }
}
