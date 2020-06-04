package com.mekcone.excrud.codegen.model.module.impl.springboot.component;

import lombok.Data;

import java.util.List;

@Data
public class Dependency {
    private String groupId;
    private String artifactId;
    private String version;
    private String scope;
    private List<Dependency> exclusions;

    public Dependency() {}

    public Dependency(String groupId, String artifactId, String version, String scope) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.scope = scope;
    }
}
