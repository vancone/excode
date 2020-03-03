package com.mekcone.incrud.model.project.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mekcone.incrud.util.StringUtil;

public class Column {
    private String name;
    private String type;
    private boolean primaryKey;

    public String getName() {
        return name;
    }

    @JsonIgnore
    public String getCamelName() {
        return StringUtil.camel(getName());
    }

    @JsonIgnore
    public String getCapitalizedCamelName() {
        return StringUtil.capitalizedCamel(getName());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
