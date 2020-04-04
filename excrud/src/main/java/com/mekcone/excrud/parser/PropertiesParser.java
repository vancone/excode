package com.mekcone.excrud.parser;

import com.mekcone.excrud.util.FileUtil;
import com.mekcone.excrud.util.LogUtil;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PropertiesParser {

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

    public static PropertiesParser parse(String propertiesText) {
        PropertiesParser propertiesParser = new PropertiesParser();
        String[] properties = propertiesText.split("\n");
        for (String property: properties) {
            String[] keyAndValue = property.split("=");
            if (keyAndValue.length == 1) {
                LogUtil.warn("No value found at \"" + property + "\"");
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
        return  propertiesParser;
    }

    public static PropertiesParser readFrom(String url) {
        String content = FileUtil.read(url);
        if (content != null && !content.isEmpty()) {
            return PropertiesParser.parse(content);
        }
        return null;
    }
}
