package com.mekcone.autocrud.core.model.project;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.ArrayList;
import java.util.List;

public class Project {
    private String groupId;
    private Value artifactId = new Value();
    private String version;
    private String description;
    private List<Module> modules = new ArrayList<>();
    private List<Table> tables = new ArrayList<>();
    private IntegerProperty integerProperty = new SimpleIntegerProperty();

    public Project() {
        integerProperty.setValue(0);
    }

    public IntegerProperty getIntegerProperty() {
        return this.integerProperty;
    }

    public void updated() {
        integerProperty.setValue(integerProperty.getValue() + 1);
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
        updated();
    }

    public Value getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId.setData(artifactId);
        updated();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Module> getModules() {
        return modules;
    }

    public void setModules(List<Module> modules) {
        this.modules = modules;
    }

    public void addModule(Module module) {
        this.modules.add(module);
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
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String string = gson.toJson(this);
        return string;
    }
}
