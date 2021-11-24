package com.vancone.excode.generator.entity;

import com.vancone.excode.generator.enums.SolutionType;
import lombok.Data;

/**
 * @author Tenton Lien
 * @since 2021/11/23
 */
@Data
public class DataAccessSolution {
    private String id;
    private SolutionType type;
    private String projectId;

    private String groupId;
    private String artifactId;
    private String version;
}
