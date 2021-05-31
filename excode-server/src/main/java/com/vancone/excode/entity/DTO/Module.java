package com.vancone.excode.entity.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Tenton Lien
 * @date 9/12/2020
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Module {

    private String id;

    private ModuleType type;

    private String name;

    private List<Extension> extensions;

//    private Project project;

    private Map<String, String> properties;
}
