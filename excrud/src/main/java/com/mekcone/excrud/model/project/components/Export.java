package com.mekcone.excrud.model.project.components;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class Export {
    @JacksonXmlProperty(isAttribute = true)
    private String type;

    @JacksonXmlProperty(isAttribute = true)
    private boolean enable;

    @JacksonXmlElementWrapper(localName = "plugins")
    @JacksonXmlProperty(localName = "plugin")
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
