package com.vancone.excode.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vancone.excode.core.model.datasource.MysqlDataSource;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tenton Lien
 * @since 7/24/2021
 */
@Data
@Document("project")
public class Project {
    @Id
    private String id;
    private String groupId;
    private String artifactId;
    private String version;
    private List<String> languages;
    private String name;
    private String description;
    private List<Module> modules;
    private DataSource datasource;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdTime;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedTime;

    @Data
    public static class DataSource {
        private MysqlDataSource mysql;
    }
}
