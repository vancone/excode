package com.vancone.excode.generator.entity;

import com.vancone.excode.generator.enums.SolutionType;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Tenton Lien
 * @since 2021/11/23
 */
@Data
@Entity
@Table
public class DataAccessSolution {
    @Id
    @GeneratedValue(generator = "jpa-uuid")
    private String id;
    private SolutionType type;
    private String projectId;

    @Data
    public static class SpringBootSolution {
        private String groupId;
        private String artifactId;
        private String version;
    }
}
