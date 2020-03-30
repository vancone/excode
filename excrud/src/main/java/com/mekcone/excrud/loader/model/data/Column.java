package com.mekcone.excrud.loader.model.data;

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

    @JacksonXmlProperty(isAttribute = true)
    private boolean filter;

    @JacksonXmlProperty(isAttribute = true)
    private String description;

    @JsonIgnore
    public String getCamelName(String tableName) {
        String[] stringArray = getName().split("_");
        String name = "";
        if (stringArray[0].equals(tableName)) {
            for (int i = 1; i < stringArray.length; i ++) {
                name += stringArray[i];
            }
        } else {
            name = getName();
        }
        return StringUtil.camel(name);
    }

    @JsonIgnore
    public String getCapitalizedCamelName(String tableName) {
        String[] stringArray = getName().split("_");
        String name = "";
        if (stringArray[0].equals(tableName)) {
            for (int i = 1; i < stringArray.length; i ++) {
                name += stringArray[i];
            }
        } else {
            name = getName();
        }
        return StringUtil.capitalizedCamel(name);
    }
}
