package com.vancone.excode.entity.DTO.module;

import com.vancone.excode.entity.DTO.extension.Extension;
import com.vancone.excode.entity.DTO.extension.Jasypt;
import com.vancone.excode.entity.DTO.extension.Lombok;
import com.vancone.excode.entity.DTO.extension.Swagger;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Tenton Lien
 * @date 6/6/2021
 */
@Data
public class SpringBootModule implements Module {
    private String groupId;
    private String artifactId;
    private String version;
    private Map<String, Extension> extensions = new HashMap<>();

    public SpringBootModule() {
        extensions.put("jasypt", new Jasypt());
        extensions.put("lombok", new Lombok());
        extensions.put("swagger", new Swagger());
    }
}
