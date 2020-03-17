package com.mekcone.excrud.model.project.components;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(Include.NON_NULL)
public class Dependency {
    private String groupId;
    private String artifactId;
    private String version;
    private String scope;
    private List<Dependency> exclusions;
}
