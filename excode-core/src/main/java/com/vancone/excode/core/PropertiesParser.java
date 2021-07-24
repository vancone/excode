package com.vancone.excode.core;

import cn.hutool.core.lang.Pair;
import com.vancone.excode.core.util.FileUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Tenton Lien
 */
@Slf4j
public class PropertiesParser {

    @Getter @Setter
    private String name;

    private List<Pair<String, String>> properties = new ArrayList<>();

    public void add(String key, String value) {
        properties.add(new Pair(key, value));
    }

    public void addSeparator() {
        properties.add(new Pair("GROUP_SEPARATOR", "NEW_LINE"));
    }

    public void combine(PropertiesParser propertiesParser) {
        this.properties.addAll(propertiesParser.properties);
        this.addSeparator();
    }

    public boolean exist(String key) {
        if (get(key) != null) {
            return true;
        }
        return false;
    }

    public String generate() {
        String outputContent = "";
        for (Pair propertyPair: properties) {
            if (propertyPair.getKey().equals("GROUP_SEPARATOR")) {
                outputContent += "\n";
                continue;
            }
            outputContent += propertyPair.getKey() + " = " + propertyPair.getValue() + "\n";
        }
        return outputContent;
    }

    public String get(String key) {
        for (Pair<String, String> property: properties) {
            if (property.getKey().equals(key)) {
                return property.getValue();
            }
        }
        return null;
    }

    public static PropertiesParser parse(String propertiesText) {
        PropertiesParser propertiesParser = new PropertiesParser();
        propertiesParser.setName("unnamed");
        String[] properties = propertiesText.split("\n");
        for (String property: properties) {
            String[] keyAndValue = property.split("=");
            if (keyAndValue.length == 1) {
                continue;
            } else if (keyAndValue.length == 2) {
                String key = keyAndValue[0].trim();
                String value = keyAndValue[1].trim();
                propertiesParser.properties.add(new Pair<>(key, value));
            } else if (keyAndValue.length > 2) {
                String key = keyAndValue[0].trim();
                String value = "";
                for (int i = 1; i < keyAndValue.length; i ++) {
                    value += keyAndValue[i];
                    if (i + 1 != keyAndValue.length) {
                        value += "=";
                    }
                }
                propertiesParser.properties.add(new Pair<>(key, value));
            }
        }
        return propertiesParser;
    }

    public static PropertiesParser readFrom(String url) {
        String content = FileUtil.read(url);
        if (StringUtils.isNotBlank(content)) {
            String fileName = new File(url).toPath().getFileName().toString();
            PropertiesParser propertiesParser = PropertiesParser.parse(content);
            propertiesParser.setName(fileName.substring(0, fileName.lastIndexOf(".")));
            return propertiesParser;
        }
        return null;
    }
}
