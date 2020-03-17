package com.mekcone.excrud.model.project.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;

@Data
public class Config {
    private String key;
    private List<String> values;

    @JsonIgnore
    public String getValue() {
        if (values.isEmpty()) {
            return null;
        }
        return values.get(0);
    }
}