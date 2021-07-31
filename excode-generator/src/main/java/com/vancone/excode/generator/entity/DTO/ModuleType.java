package com.vancone.excode.generator.entity.DTO;

import lombok.Data;

import java.util.Map;

/**
 * @author Tenton Lien
 * @date 9/14/2020
 */
@Data
public class ModuleType {

    private String id;

    private Map<String, String> name;

    private String description;

    private String publisher;
}
