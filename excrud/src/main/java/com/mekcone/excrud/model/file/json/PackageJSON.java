package com.mekcone.excrud.model.file.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import java.util.*;

public class PackageJSON {
    private String name;
    private String version;
    private String description;
    private String author;

    @SerializedName("private")
    @JsonProperty("private")
    private boolean isPrivate;

    private Map scripts = new LinkedHashMap();
    private Map dependencies = new LinkedHashMap();
    private Map devDependencies = new LinkedHashMap();
    private Map engines = new LinkedHashMap();

    @SerializedName("browserslist")
    @JsonProperty("browserslist")
    private List<String> browsers = new ArrayList<>();

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    /*public boolean isPrivate() {
        return this.isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }*/

    /*public Map getScripts() {
        return scripts;
    }

    public void setScripts(Map scripts) {
        this.scripts = scripts;
    }

    public void addScript(String key, String value) {
        this.scripts.put(key, value);
    }

    public void addDependency(String key, String value) {
        this.dependencies.put(key, value);
    }

    public void addDevDependency(String key, String value) {
        this.devDependencies.put(key, value);
    }

    public void addEngine(String key, String value) {
        this.engines.put(key, value);
    }

    public void addBrowser(String value) {
        this.browsers.add(value);
    }*/
}
