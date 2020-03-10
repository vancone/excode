package com.mekcone.excrud.model.project.components;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Config {
    private String key;
    private List<String> values;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @JsonIgnore
    public String getValue() {
        if (values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }
}