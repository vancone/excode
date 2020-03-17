package com.mekcone.excrud.model.project.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mekcone.excrud.util.StringUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {
    private String name;
    private String description;
    private String primaryKey;
    private List<Column> columns = new ArrayList<>();

    @JsonIgnore
    public String getCamelName() {
        return StringUtil.camel(getName());
    }

    @JsonIgnore
    public String getCapitalizedCamelName() {
        return StringUtil.capitalizedCamel(getName());
    }

    @JsonIgnore
    public String getCamelPrimaryKey() {
        return StringUtil.camel(getPrimaryKey());
    }

    @JsonIgnore
    public boolean isPrimaryKeyBlank() {
        if (primaryKey == null || primaryKey.isEmpty()) {
            return true;
        }
        return false;
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }
}
