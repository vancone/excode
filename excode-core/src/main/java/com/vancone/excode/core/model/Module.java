package com.vancone.excode.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 * @since 7/24/2021
 */
@Data
public class Module {
    private String type;
    private String name;
    private boolean enable;
    private List<Module> extensions = new ArrayList<>();
    private Map<String, String> properties = new HashMap<>();

    @JsonIgnore
    public String getProperty(String property) {
        for (String key: properties.keySet()) {
            if (property.equals(key)) {
                return properties.get(key);
            }
        }
        return null;
    }
}
