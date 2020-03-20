package com.mekcone.excrud.model.springboot;

import com.mekcone.excrud.model.project.components.Dependency;
import com.mekcone.excrud.model.project.components.Property;
import lombok.Data;

import java.util.List;

@Data
public class PluginInfo {
    private List<Dependency> dependencies;
    private List<Property> properties;
}
