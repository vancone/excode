package com.vancone.excode.core.util;

import com.vancone.excode.core.model.datasource.MysqlDataSource;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Tenton Lien
 */
public class SqlUtil {

    public static String createDatabase(MysqlDataSource database) {
        return "CREATE DATABASE " + database.getConnection().getDatabase() + ";";
    }

    public static String createTable(MysqlDataSource.Table table) {
        String sql = "CREATE TABLE " + table.getName() + "(\n";
        for (int i = 0; i < table.getColumns().size(); i ++) {
            MysqlDataSource.Table.Column column = table.getColumns().get(i);
            sql += "    `" + column.getName() + "` " + column.getType().toUpperCase();
            if (column.getType().equals("varchar")) {
                sql += "(" + column.getLength() + ")";
            }
            if (column.isPrimaryKey()) {
                sql += " PRIMARY KEY NOT NULL";
            }

            if (StringUtils.isNotBlank(column.getComment())) {
                sql += " COMMENT '" + column.getComment() + "'";
            }

            if (i == table.getColumns().size() - 1) {
                sql += "\n";
            } else {
                sql += ",\n";
            }
        }
        sql += ");";
        return sql;
    }

    public static String insertQuery(MysqlDataSource.Table table, boolean ignorePrimaryKey) {
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

    public static String updateQuery(MysqlDataSource.Table table) {
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
        query += " WHERE " + table.getPrimaryKeyName() + "=#{" + StrUtil.camelCase(table.getPrimaryKeyName()) + "}";
        return query;
    }
}
