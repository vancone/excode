package com.vancone.devops.codegen.util;

import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*
 * Author: Tenton Lien
 * Date: 6/18/2020
 */

public class MdUtil {
    public static String codeBlock(String content) {
        return "```\n" + content + "\n```";
    }

    public static String heading(String content, int level) {
        StringBuilder contentBuilder = new StringBuilder(" " + content + "\n");
        while (level > 0) {
            contentBuilder.insert(0, "#");
            level --;
        }
        content = contentBuilder.toString();
        return content;
    }

    public static String lineBreak() {
        return "\n";
    }

    public static String list(String content) {
        return "- " + content + "\n";
    }

    public static String underline(String content) {
        return "<u>" + content + "</u>";
    }

    // Generate table with Markdown syntax
    public static class Table {
        @Setter
        protected String[] headerFields;

        protected List<String[]> rows = new ArrayList<>();

        public void setHeaderFields(String[] headerFields) {
            this.headerFields = headerFields;
        }

        public void addRow(String[] rowFields) {
            this.rows.add(rowFields);
        }

        @Override
        public String toString() {

            // Header
            String content = "|";
            for (String headField: headerFields) {
                content += " " + headField + " |";
            }
            content += "\n";
            content += "|";
            for (int i = 0; i < headerFields.length; i ++) {
                content += " ---- |";
            }
            content += "\n";

            // Body
            for (String[] row: rows) {
                content += "|";
                for (String rowField: row) {
                    content += " " + rowField + " |";
                }
                content += "\n";
            }

            return content;
        }
    }

    // Generate table with HTML Syntax, supporting colspan, rowspan, etc.
    public static class ComplexTable extends Table {
        @Override
        public String toString() {
            String content = "<table>";

            // Header
            content += "<tr>\n";
            for (String headField: headerFields) {
                content += "<th>" + headField + "</th>\n";
            }
            content += "</tr>\n";

            // Body
            for (String[] row: rows) {
                content += "<tr>\n";
                for (String rowField: row) {
                    content += "<td>" + rowField + "</td>\n";
                }
                content += "</tr>\n";
            }
            content += "</table>\n";
            return content;
        }
    }
}
