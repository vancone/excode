package com.mekcone.excrud.model.project.components;

import lombok.Data;

import java.util.List;

@Data
public class Export {
    private String type;
    private List<Plugin> plugins;

    public Plugin getPlugin(String name) {
        for (Plugin plugin: plugins) {
            if (plugin.getName().equals(name)) {
                return plugin;
            }
        }
        return null;
    }
}
