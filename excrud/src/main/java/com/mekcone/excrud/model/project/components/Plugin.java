package com.mekcone.excrud.model.project.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Plugin {
    private String name;
    private boolean enable;
    private List<Config> configs;

    @JsonIgnore
    public Config getConfig(String configKey) {
        for (Config config: configs) {
            if (config.getKey().equals(configKey)) {
                return config;
            }
        }
        return null;
    }
}
