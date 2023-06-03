package com.vancone.excode.util;

import com.vancone.excode.entity.DataStoreOld;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Tenton Lien
 */
public class SqlUtil {

    public static String createDatabase(DataStoreOld store) {
        return "CREATE DATABASE " + store.getConnection().getDatabase() + ";";
    }

    public static String createTable(DataStoreOld store) {
        String sql = "CREATE TABLE " + store.getName() + "(\n";
        for (int i = 0; i < store.getNodes().size(); i ++) {
            DataStoreOld.Node node = store.getNodes().get(i);
            sql += "    `" + node.getName() + "` " + node.getType().toUpperCase();
            if (node.getType().equals("varchar")) {
                sql += "(" + node.getLength() + ")";
            }
            if (node.isPrimaryKey()) {
                sql += " PRIMARY KEY NOT NULL";
            }

            if (StringUtils.isNotBlank(node.getComment())) {
                sql += " COMMENT '" + node.getComment() + "'";
            }

            if (i == store.getNodes().size() - 1) {
                sql += "\n";
            } else {
                sql += ",\n";
            }
        }
        sql += ");";
        return sql;
    }

    public static String insertQuery(DataStoreOld store, boolean ignorePrimaryKey) {
        String query = "INSERT INTO " + store.getName() + " (";
        // If the last column is a primary key, this block of codes may not work out the correct result
        for (int i = 0; i < store.getNodes().size(); i ++) {
            if (ignorePrimaryKey && store.getNodes().get(i).isPrimaryKey()) {
                continue;
            }
            query += store.getNodes().get(i).getName();
            if (i + 1 != store.getNodes().size()) {
                query += ", ";
            } else {
                query +=") ";
            }
        }
        query += "VALUES(";
        for (int i = 0; i < store.getNodes().size(); i ++) {
            if (ignorePrimaryKey && store.getNodes().get(i).isPrimaryKey()) {
                continue;
            }
            query += "#{" + store.getNodes().get(i).getName() + "}";
            if (i + 1 != store.getNodes().size()) {
                query += ", ";
            } else {
                query += ")";
            }
        }
        return query;
    }

    public static String updateQuery(DataStoreOld store) {
        String query = "UPDATE " + store.getName() + " SET ";
        for (int i = 0; i < store.getNodes().size(); i ++) {
            if (i == 0) {
                continue;
            }
            query += store.getNodes().get(i).getName() + "=#{" + store.getNodes().get(i).getName() + "}";
            if (i + 1 != store.getNodes().size()) {
                query += ", ";
            }
        }
        query += " WHERE " + store.getNodes().get(0).getName() + "=#{" + StrUtil.toCamelCase(store.getNodes().get(0).getName()) + "}";
        return query;
    }
}
