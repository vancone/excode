package com.vancone.excode.core.model.datasource;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vancone.excode.core.util.StrUtil;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.List;

/**
 * @author Tenton Lien
 * @since 7/24/2021
 */
@Data
public class MysqlDataSource {
    private boolean sqlGen;
    private Connection connection;
    private List<Table> tables;

    @Data
    public static class Connection {
        private String host;
        private int port;
        private String database;
        private String username;
        private String password;
        private String timezone;
    }

    @Data
    public static class Table {
        private String name;
        private String description;
        private List<Column> columns;

        @Transient
        @JsonIgnore
        private String primaryKeyName;

        public String getUpperCamelCaseName() {
            return StrUtil.upperCamelCase(name);
        }

        @Data
        public static class Column {
            private String name;
            private String type;
            private int length;
            private boolean primaryKey;
            private String comment;

            @JsonIgnore
            public String getCamelCaseName(String tableName) {
                String[] stringArray = getName().split("_");
                String name = "";
                if (stringArray[0].equals(tableName)) {
                    for (int i = 1; i < stringArray.length; i ++) {
                        name += stringArray[i];
                    }
                } else {
                    name = getName();
                }
                return StrUtil.camelCase(name);
            }

            @JsonIgnore
            public String getUpperCamelCaseName(String tableName) {
                return StrUtil.capitalize(getCamelCaseName(tableName));
            }
        }
    }
}
