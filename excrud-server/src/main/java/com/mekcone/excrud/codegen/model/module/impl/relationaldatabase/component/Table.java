package com.mekcone.excrud.codegen.model.module.impl.relationaldatabase.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.codegen.util.StrUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Table {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @JacksonXmlProperty(isAttribute = true)
    private String primaryKey;

    @JacksonXmlElementWrapper(localName = "columns")
    @JacksonXmlProperty(localName = "column")
    private List<Column> columns = new ArrayList<>();

    @JacksonXmlProperty(isAttribute = true)
    private String catalogueOf;

    @JsonIgnore
    public String getCamelCaseName() {
        return StrUtil.camelCase(getName());
    }

    @JsonIgnore
    public String getUpperCamelCaseName() {
        return StrUtil.upperCamelCase(getName());
    }

    @JsonIgnore
    public String getCamelCasePrimaryKey() {
        return StrUtil.camelCase(getPrimaryKey());
    }

    @JsonIgnore
    public boolean hasPrimaryKey() {
        return !(primaryKey == null || primaryKey.isEmpty());
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public boolean hasColumn() {
        return !(columns.isEmpty());
    }
}
