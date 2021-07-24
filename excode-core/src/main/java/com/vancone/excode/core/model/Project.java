package com.vancone.excode.core.model;

import com.vancone.excode.core.model.datasource.DataSource;
import lombok.Data;

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
}
