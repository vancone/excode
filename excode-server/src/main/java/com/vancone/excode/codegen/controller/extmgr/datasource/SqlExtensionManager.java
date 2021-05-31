package com.vancone.excode.codegen.controller.extmgr.datasource;

import com.vancone.excode.codegen.model.database.Database;
import com.vancone.excode.codegen.model.database.Table;

public class SqlExtensionManager {

    public static String createDatabaseQuery(Database database) {
        return "CREATE DATABASE " + database.getName() + ";";
    }

    public static String createTableQuery(Table table) {
        String query = "CREATE TABLE " + table.getName() + " (\n";
        for (int i = 0; i < table.getColumns().size(); i++) {
            query += "    " + table.getColumns().get(i).getName() + " " + table.getColumns().get(i).getType();

            Integer dataLength = table.getColumns().get(i).getLength();
            if (dataLength != null && dataLength > 0) {
                query += "(" + table.getColumns().get(i).getLength() + ")";
            }
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
}
