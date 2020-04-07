package com.mekcone.excrud.model.project;

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
        return (value != null ? value : "");
    }

    public boolean getBooleanProperty(String key) {
        var value = properties.get(key);
        return !(value != null && value.toLowerCase().equals("false"));
    }

    public boolean existProperty(String key) {
        var value = properties.get(key);
        return (value != null);
    }
}
