package com.vancone.monster.code.codegen.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vancone.monster.code.codegen.util.StrUtil;
import lombok.Data;

@Data
public class Column {
    private String name;

    private String type;

    private Integer length;

    private boolean primaryKey;

    private String bind;

    private boolean filter;

    private boolean detail;

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
