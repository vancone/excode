package com.vancone.excode.core.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.vancone.excode.core.util.StrUtil;
import lombok.Data;

/**
 * @author Tenton Lien
 */
@Data
public class Column {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private Integer length;

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
        String[] stringArray = getName().split("_");
        String name = "";
        if (stringArray[0].equals(tableName)) {
            for (int i = 1; i < stringArray.length; i ++) {
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
