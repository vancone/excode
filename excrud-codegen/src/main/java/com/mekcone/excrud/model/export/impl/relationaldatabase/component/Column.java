package com.mekcone.excrud.model.export.impl.relationaldatabase.component;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.mekcone.excrud.util.StrUtil;
import lombok.Data;

@Data
public class Column {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private boolean primaryKey;

    @JacksonXmlProperty(isAttribute = true)
    private String bind;

    @JacksonXmlProperty(isAttribute = true)
    private boolean filter;

    @JacksonXmlProperty(isAttribute = true)
    private boolean detail;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @JsonIgnore
    public String getCamelCaseName(String tableName) {
        var stringArray = getName().split("_");
        String name = "";
        if (stringArray[0].equals(tableName)) {
            for (var i = 1; i < stringArray.length; i ++) {
                name += stringArray[i];
            }
        } else {
            name = getName();
        }
        return StrUtil.camelCase(name);
    }

    @JsonIgnore
    public String getUpperCamelCaseName(String tableName) {
        return StrUtil.capitalize(getCamelCaseName(tableName));
    }
}
