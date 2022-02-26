package com.vancone.excode.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vancone.excode.core.enums.DataCarrier;
import com.vancone.excode.core.enums.DataStoreType;
import com.vancone.excode.core.util.StrUtil;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 2021/11/18
 */
@Data
@Document("data_store")
public class DataStore {
    private String id;

    private String projectId;

    private String name;

    private DataStoreType type;

    private DataCarrier carrier;

    private String description;

    private Connection connection;

    private List<Node> nodes;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @Data
    public static class Node {
        private String name;
        private String type;
        private Integer length;
        private String comment;
        private List<Node> children;
        private boolean primaryKey;
        private boolean foreignKey;

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

    @Data
    public static class Connection {
        private String host;
        private Integer port;
        private String username;
        private String password;
        private String database;
        private String timezone;
    }

    @JsonIgnore
    public String getPrimaryKeyName() {
        for (Node node: nodes) {
            if (node.isPrimaryKey()) {
                return node.getName();
            }
        }
        return nodes.get(0).getName();
    }
}
