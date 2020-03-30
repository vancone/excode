package com.mekcone.excrud.loader.model.components;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.Map;

@Data
public class Export {
    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private boolean enable;

    private Map<String, String> properties;

    public String getProperty(String key) {
        String value = properties.get(key);
        if (value != null) {
            return value;
        } else {
            return "";
        }
    }
}
