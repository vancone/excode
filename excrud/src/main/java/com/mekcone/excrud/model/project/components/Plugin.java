package com.mekcone.excrud.model.project.components;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class Plugin {
    private String name;
    private boolean enable;
    private List<Config> configs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

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
