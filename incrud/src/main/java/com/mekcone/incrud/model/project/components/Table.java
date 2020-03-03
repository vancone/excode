package com.mekcone.incrud.model.project.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mekcone.incrud.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class Table {
    private String name;
    private String primaryKey;
    private List<Column> columns = new ArrayList<>();

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

    public String getPrimaryKey() {
        return primaryKey;
    }

    @JsonIgnore
    public String getCamelPrimaryKey() {
        return StringUtil.camel(getPrimaryKey());
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    @JsonIgnore
    public boolean isPrimaryKeyBlank() {
        if (primaryKey == null || primaryKey.isEmpty()) {
            return true;
        }
        return false;
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }
}
