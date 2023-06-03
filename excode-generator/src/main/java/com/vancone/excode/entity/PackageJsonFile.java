package com.vancone.excode.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author Tenton Lien
 * @since 2021/07/25
 */
@Data
public class PackageJsonFile {
    private String name;
    private String version;
    private Map<String, String> scripts;
    private Map<String, String> dependencies;
    private Map<String, String> devDependencies;
}
