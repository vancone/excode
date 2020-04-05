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
        if (value != null) {
            return value;
        } else {
            return "";
        }
    }

    public boolean getBooleanProperty(String key) {
        String value = properties.get(key);
        if (value != null && value.toLowerCase().equals("false")) {
            return false;
        } else {
            // Default return value
            return true;
        }
    }

    public boolean existProperty(String key) {
        String value = properties.get(key);
        if (value != null) {
            return true;
        } else {
            return false;
        }
    }
}
