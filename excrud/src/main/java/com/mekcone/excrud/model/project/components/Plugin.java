package com.mekcone.excrud.model.project.components;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Plugin {
    @JacksonXmlProperty(isAttribute = true)
    private String name;

    @JacksonXmlProperty(isAttribute = true)
    private boolean enable;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "config")
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
