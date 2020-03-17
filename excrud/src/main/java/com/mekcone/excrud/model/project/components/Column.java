package com.mekcone.excrud.model.project.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mekcone.excrud.util.StringUtil;
import lombok.Data;

@Data
public class Column {
    private String name;
    private String type;
    private boolean primaryKey;

    @JsonIgnore
    public String getCamelName() {
        return StringUtil.camel(getName());
    }

    @JsonIgnore
    public String getCapitalizedCamelName() {
        return StringUtil.capitalizedCamel(getName());
    }
}
