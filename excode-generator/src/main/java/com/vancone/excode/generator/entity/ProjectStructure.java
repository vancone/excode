package com.vancone.excode.generator.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vancone.excode.generator.enums.TemplateType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * @author Tenton Lien
 * @since 2022/05/19
 */
@Data
@Document("project_structure")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectStructure {

    private String module;

    private String label;

    private TemplateType type;

    private List<ProjectStructure> children;
}
