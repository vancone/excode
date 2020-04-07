package com.mekcone.excrud.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.util.StrUtil;
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

    @JsonIgnore
    public String getCamelName() {
        return StrUtil.camel(getName());
    }

    @JsonIgnore
    public String getCapitalizedCamelName() {
        return StrUtil.capitalizedCamel(getName());
    }

    @JsonIgnore
    public String getCamelPrimaryKey() {
        return StrUtil.camel(getPrimaryKey());
    }

    @JsonIgnore
    public boolean isPrimaryKeyBlank() {
        return (primaryKey == null || primaryKey.isEmpty());
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public boolean hasColumn() {
        return !(columns.isEmpty());
    }
}
