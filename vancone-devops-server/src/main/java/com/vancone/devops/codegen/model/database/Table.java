package com.vancone.devops.codegen.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vancone.devops.codegen.util.StrUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Data
public class Table {
    private String name;

    private String description;

    private String primaryKey;

    private List<Column> columns = new ArrayList<>();

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
        return StringUtils.isNotBlank(primaryKey);
    }

    public void addColumn(Column column) {
        this.columns.add(column);
    }

    public boolean hasColumn() {
        return !(columns.isEmpty());
    }
}
