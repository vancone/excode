package com.mekcone.excrud.generator.impl;

import com.mekcone.excrud.Application;
import com.mekcone.excrud.generator.Generator;
import com.mekcone.excrud.model.project.Project;
import com.mekcone.excrud.model.database.Database;
import com.mekcone.excrud.model.database.Table;
import com.mekcone.excrud.util.FileUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SqlGenerator implements Generator {

    public SqlGenerator(Project project) {
        //FileUtil.checkDirectory("sql");
        String code = "";
        if (Application.description != null) {
            code += "-- " + Application.description + "\n\n";
        }
        for (var database: project.getDatabases()) {
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
            query += "#{" + table.getColumns().get(i).getCamelName(table.getName()) + "}";
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
            query += table.getColumns().get(i).getName() + "=#{" + table.getColumns().get(i).getCamelName(table.getName()) + "}";
            if (i + 1 != table.getColumns().size()) {
                query += ", ";
            }
        }
        query += " WHERE " + table.getPrimaryKey() + "=#{" + table.getCamelPrimaryKey() + "}";
        return query;
    }

    @Override
    public void generate() {

    }
}