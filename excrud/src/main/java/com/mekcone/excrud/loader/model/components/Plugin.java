package com.mekcone.excrud.loader.model.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class Plugin {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private boolean enable;

    @JacksonXmlElementWrapper(localName = "configs")
    @JacksonXmlProperty(localName = "config")
    private List<Config> configs;

    @JacksonXmlElementWrapper(localName = "dependencies")
    @JacksonXmlProperty(localName = "dependency")
    private List<Dependency> dependencies;

    @JacksonXmlElementWrapper(localName = "properties")
    @JacksonXmlProperty(localName = "property")
    private List<Property> properties;

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
