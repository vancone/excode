package com.mekcone.incrud.model.file.properties;

import com.mekcone.incrud.model.file.FileModel;
import com.mekcone.incrud.model.project.components.Dependency;
import com.mekcone.incrud.model.project.components.Property;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationPropertiesFileModel extends FileModel {
    public List<Dependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies(List<Dependency> dependencies) {
        this.dependencies = dependencies;
    }

    private List<Dependency> dependencies;

    @Override
    public String toString() {
        String applicationPropertiesText = "";
        applicationPropertiesText += "# " + getDescription() + "\n\n";
        for (Dependency dependency: this.dependencies) {
            applicationPropertiesText += "# " + dependency.getLabel() + "\n";
            if (dependency.getProperties() != null) {
                for (Property property: dependency.getProperties()) {
                    applicationPropertiesText += property.getKey() + " = " + property.getValue() + "\n";
                }
                applicationPropertiesText += "\n";
            }
        }
        return applicationPropertiesText;
    }
}
