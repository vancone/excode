package com.mekcone.excrud.model.project.components;

import java.util.List;

public class Export {
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }

    public Plugin getPlugin(String name) {
        for (Plugin plugin: plugins) {
            if (plugin.getName().equals(name)) {
                return plugin;
            }
        }
        return null;
    }

    public void setPlugins(List<Plugin> plugins) {
        this.plugins = plugins;
    }

    private String type;
    private List<Plugin> plugins;
}
