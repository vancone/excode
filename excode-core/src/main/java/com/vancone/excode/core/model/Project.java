package com.vancone.excode.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vancone.excode.core.model.datasource.MysqlDataSource;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 * @date 7/24/2021
 */
@Data
public class Project {
    private String groupId;
    private String artifactId;
    private String version;
    private List<String> languages;
    private Map<String, String> name;
    private Map<String, String> description;
    private List<Module> modules;
    private DataSource datasource;

    @JsonIgnore
    public String getDefaultName() {
        if (name != null) {
            String defaultLanguage = name.get("default");
            if (StringUtils.isNotBlank(defaultLanguage)) {
                return name.get(defaultLanguage);
            }
        }
        return "";
    }

    @JsonIgnore
    public String getDefaultDescription() {
        if (description != null) {
            String defaultLanguage = description.get("default");
            if (StringUtils.isNotBlank(defaultLanguage)) {
                return description.get(defaultLanguage);
            }
        }
        return "";
    }

    @Data
    public static class DataSource {
        private MysqlDataSource mysql;
    }
}
