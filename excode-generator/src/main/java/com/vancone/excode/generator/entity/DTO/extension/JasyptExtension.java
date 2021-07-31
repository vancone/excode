package com.vancone.excode.generator.entity.DTO.extension;

import com.vancone.excode.generator.codegen.annotation.ExtensionClass;
import com.vancone.excode.generator.constant.ModuleConstant;
import lombok.Data;

/**
 * @author Tenton Lien
 * @date 6/6/2021
 */
@Data
@ExtensionClass(module = ModuleConstant.SPRING_BOOT)
public class JasyptExtension extends BaseExtension {
    private String password;
}
