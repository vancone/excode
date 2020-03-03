package com.mekcone.incrud.model.project.components;

public class Property {
    private String key;
    private String value;
    private String defaultValue;
    private String prefix;

    public String getKey() {
        return this.key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
