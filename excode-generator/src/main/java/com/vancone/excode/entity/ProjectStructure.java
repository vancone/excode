package com.vancone.excode.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.vancone.excode.enums.TemplateType;
import com.vancone.excode.util.JsonUtil;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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

    public ProjectStructure() {}

    public ProjectStructure(String label, TemplateType type) {
        this.label = label;
        this.type = type;
    }

    public void addNode(String path, ProjectStructure node) {
        String[] packages = path.split("__");
        ProjectStructure currentNode = this;
        for (String pack: packages) {
            for (ProjectStructure childNode: currentNode.children) {
                if (childNode.getLabel().equals(pack)) {
                    currentNode = childNode;
                    break;
                }
            }
        }
        if (currentNode.getChildren() == null) {
            currentNode.setChildren(new ArrayList<>());
        }
        currentNode.getChildren().add(node);
    }
}
