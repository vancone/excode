package com.mekcone.excrud.model.project.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.util.StringUtil;
import lombok.Data;

@Data
public class Column {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
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
