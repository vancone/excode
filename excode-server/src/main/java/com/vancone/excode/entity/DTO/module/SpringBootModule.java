package com.vancone.excode.entity.DTO.module;

import com.vancone.excode.codegen.util.StrUtil;
import com.vancone.excode.entity.DTO.extension.BaseExtension;
import com.vancone.excode.entity.DTO.extension.JasyptExtension;
import com.vancone.excode.entity.DTO.extension.LombokExtension;
import com.vancone.excode.entity.DTO.extension.SwaggerExtension;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private List<BaseExtension> extensions = new ArrayList<>();

    public SpringBootModule() {
        extensions.add(new JasyptExtension());
        extensions.add(new LombokExtension());
        extensions.add(new SwaggerExtension());
    }

    public Map<String, BaseExtension> getExtensions() {
        Map<String, BaseExtension> extensionMap = new HashMap<>(extensions.size());
        for (BaseExtension extension: extensions) {
            String extensionName = extension.getClass().getName();
            extensionName = StrUtil.snakeCase(extensionName.substring(extensionName.lastIndexOf('.') + 1, extensionName.length() - 9));
            extensionMap.put(extensionName, extension);
        }
        return extensionMap;
    }
}
