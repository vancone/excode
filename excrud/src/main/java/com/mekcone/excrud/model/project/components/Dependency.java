package com.mekcone.excrud.model.project.components;

import lombok.Data;

import java.util.List;

@Data
public class Dependency {
    private String groupId;
    private String artifactId;
    private String version;
    private String scope;
    private List<Dependency> exclusions;
}
