package com.mekcone.excrud.loader.model.components;

import lombok.Data;

@Data
public class Property {
    private String key;
    private String value;
    private String defaultValue;
    private String prefix;
}
