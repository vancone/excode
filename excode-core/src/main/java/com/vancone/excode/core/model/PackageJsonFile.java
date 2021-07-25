package com.vancone.excode.core.model;

import lombok.Data;

import java.util.Map;

/**
 * @author Tenton Lien
 * @date 7/25/2021
 */
@Data
public class PackageJsonFile {
    private String name;
    private String version;
    private Map<String, String> scripts;
    private Map<String, String> dependencies;
    private Map<String, String> devDependencies;
}
