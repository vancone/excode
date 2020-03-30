package com.mekcone.excrud.util;

import com.mekcone.excrud.loader.model.data.Table;

public class SqlUtil {
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
        for (int i = 0; i < table.getColumns().size(); i ++) {
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
}
