package model.project;

import controller.Logger;

import java.util.ArrayList;
import java.util.List;

public class Module {
    private String id;
    private List<Property> config = new ArrayList<>();

    public Module() {}
    public Module(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Property> getConfig() {
        return this.config;
    }

    public void setConfig(List<Property> config) {
        this.config = config;
    }

    public void addConfig(Property property) {
        this.config.add(property);
    }

    public static String getPrefix(String moduleId) {
        switch (moduleId) {
            case "mysql": return "spring.datasource.";
            case "redis": return "spring.redis.";
            default: return null;
        }
    }
}
