package com.vancone.excode.entity.microservice;

import cn.hutool.core.lang.Pair;
import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 * @since 2022/06/11
 */
@Data
public class ApplicationProperty {

    private String name;

    private String profile;

    private List<PropertyGroup> propertyGroups = new ArrayList<>();

    private List<Pair<String, String>> properties = new ArrayList<>();

    public void add(String key, String value) {
        properties.add(new Pair(key, value));
    }

    public void addSeparator() {
        properties.add(new Pair("GROUP_SEPARATOR", "NEW_LINE"));
    }

    public PropertyGroup createPropertyGroup() {
        PropertyGroup propertyGroup = new PropertyGroup();
        propertyGroups.add(propertyGroup);
        return propertyGroup;
    }

    public String get(String key) {
        for (Pair<String, String> property: properties) {
            if (property.getKey().equals(key)) {
                return property.getValue();
            }
        }
        return null;
    }

    public boolean exist(String key) {
        if (properties.contains(key)) {
            return true;
        }
        return false;
    }

    @Data
    public static class PropertyGroup {
        private String comment;

        private Map<String, String> items = new LinkedHashMap<>();

        public void add(String key, String value) {
            items.put(key, value);
        }
    }
}
