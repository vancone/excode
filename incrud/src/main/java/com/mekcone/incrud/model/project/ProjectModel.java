package com.mekcone.incrud.model.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mekcone.incrud.model.project.components.Dependency;
import com.mekcone.incrud.model.project.components.Table;
import com.mekcone.incrud.util.LogUtil;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProjectModel {
    private StringProperty groupId = new SimpleStringProperty();
    private StringProperty artifactId = new SimpleStringProperty();
    private StringProperty version = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private ObjectProperty<List<Dependency>> dependencies = new SimpleObjectProperty<>();
    private List<Table> tables = new ArrayList<>();

    public String getGroupId() {
        return groupId.getValue();
    }

    public void setGroupId(String groupId) {
        this.groupId.setValue(groupId);
    }

    public String getArtifactId() {
        return artifactId.getValue();
    }

    public void setArtifactId(String artifactId) {
        this.artifactId.setValue(artifactId);
    }

    public String getVersion() {
        return version.getValue();
    }

    public void setVersion(String version) {
        this.version.setValue(version);
    }

    public String getDescription() {
        return description.getValue();
    }

    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public List<Dependency> getDependencies() {
        return dependencies.getValue();
    }

    public void setDependencies(List<Dependency> dependencies) {
        if (this.dependencies.getValue() == null) {
            this.dependencies.setValue(new ArrayList<>());
        }
        this.dependencies.setValue(dependencies);
    }

    public void addDependency(Dependency dependency) {
        if (this.dependencies.getValue() == null) {
            this.dependencies.setValue(new ArrayList<>());
        }
        List<Dependency> dependencies = this.dependencies.getValue();
        dependencies.add(dependency);
        this.dependencies.setValue(dependencies);
    }

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public void addTable(Table table) {
        this.tables.add(table);
    }

    @Override
    public String toString() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            // return objectMapper.writeValueAsString(this);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        } catch (Exception ex) {
            LogUtil.warn(ex.getMessage());
            return null;
        }
    }
}
