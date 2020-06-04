package com.mekcone.excrud.codegen.controller.generator;

import com.mekcone.excrud.ServerApplication;
import com.mekcone.excrud.codegen.constant.ApplicationParameter;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Table;
import com.mekcone.excrud.codegen.model.project.Project;
import com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component.Database;
import com.mekcone.excrud.codegen.util.FileUtil;
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
        for (int i = 0; i < table.getColumns().size(); i++) {
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
        for (int i = 0; i < table.getColumns().size(); i ++) {
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
        for (int i = 0; i < table.getColumns().size(); i ++) {
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
        for (int i = 0; i < table.getColumns().size(); i ++) {
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
        code += "-- " + ApplicationParameter.DESCRIPTION + "\n\n";
        for (Database database: project.getModuleSet().getRelationalDatabaseModule().getDatabases()) {
            if (database != null && database.hasTable()) {
                code += createDatabaseQuery(database) + "\n\n";
                for (Table table: database.getTables()) {
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
